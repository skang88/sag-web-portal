pipeline {
    agent any

    environment {
        BACKEND_IMAGE_NAME = 'sag-portal-backend'
        FRONTEND_IMAGE_NAME = 'sag-portal-frontend'
        BACKEND_CONTAINER_NAME = 'backend'
        FRONTEND_CONTAINER_NAME = 'frontend'
        DOCKER_NETWORK = 'sag-portal-net'
        VITE_API_BACKEND_URL = 'http://172.16.220.32:5001'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Prepare Environment') {
            steps {
                sh "docker network create ${DOCKER_NETWORK} || true"
            }
        }

        stage('Build & Deploy') {
            parallel {
                stage('Build Backend') {
                    stages {
                        stage('Build Image') {
                            steps {
                                echo "Building Backend Docker image: ${env.BACKEND_IMAGE_NAME}"
                                script {
                                    docker.build(env.BACKEND_IMAGE_NAME, './backend')
                                }
                            }
                        }
                        stage('Deploy Container') {
                            steps {
                                withCredentials([
                                    string(credentialsId: 'mssql-server', variable: 'MSSQL_SERVER'),
                                    string(credentialsId: 'mssql-database', variable: 'MSSQL_DATABASE'),
                                    string(credentialsId: 'mssql-user', variable: 'MSSQL_USER'),
                                    string(credentialsId: 'mssql-password', variable: 'MSSQL_PASSWORD'),
                                    string(credentialsId: 'mssql-port', variable: 'MSSQL_PORT')
                                ]) {
                                    script {
                                        dockerStopRemove(env.BACKEND_CONTAINER_NAME)
                                        echo "Running new Backend container..."
                                        sh """docker run -d --name ${env.BACKEND_CONTAINER_NAME} \
                                            --network ${DOCKER_NETWORK} \
                                            -p 5001:5001 \
                                            --restart always \
                                            -e MSSQL_SERVER=${MSSQL_SERVER} \
                                            -e MSSQL_DATABASE=${MSSQL_DATABASE} \
                                            -e MSSQL_USER=${MSSQL_USER} \
                                            -e MSSQL_PASSWORD=${MSSQL_PASSWORD} \
                                            -e MSSQL_PORT=${MSSQL_PORT} \
                                            ${env.BACKEND_IMAGE_NAME}"""
                                    }
                                }
                            }
                        }
                    }
                }
                stage('Build Frontend') {
                    stages {
                        stage('Build Image') {
                            steps {
                                echo "Building Frontend Docker image: ${env.FRONTEND_IMAGE_NAME}"
                                script {
                                    docker.build(env.FRONTEND_IMAGE_NAME, "./frontend --build-arg VITE_API_BACKEND_URL=${env.VITE_API_BACKEND_URL}")
                                }
                            }
                        }
                        stage('Deploy Container') {
                            steps {
                                script {
                                    dockerStopRemove(env.FRONTEND_CONTAINER_NAME)
                                    echo "Running new Frontend container..."
                                    sh "docker run -d --name ${env.FRONTEND_CONTAINER_NAME} --network ${DOCKER_NETWORK} -p 8050:80 --restart always ${env.FRONTEND_IMAGE_NAME}"
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            echo "Cleaning up workspace..."
            cleanWs()
        }
    }
}

// 컨테이너를 안전하게 중지하고 삭제하는 헬퍼 함수
def dockerStopRemove(containerName) {
    sh "docker stop ${containerName} || true"
    sh "docker rm ${containerName} || true"
}