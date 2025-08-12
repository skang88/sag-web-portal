pipeline {
    agent any

    environment {
        // 로컬 배포에 사용할 이미지 및 컨테이너 이름 정의
        BACKEND_IMAGE_NAME = 'sag-portal-backend'
        FRONTEND_IMAGE_NAME = 'sag-portal-frontend'
        // Nginx 프록시가 호스트 이름으로 찾을 수 있도록 컨테이너 이름을 'backend'로 설정
        BACKEND_CONTAINER_NAME = 'backend'
        FRONTEND_CONTAINER_NAME = 'frontend'
        // 컨테이너 간 통신을 위한 네트워크
        DOCKER_NETWORK = 'sag-portal-net'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Prepare Environment') {
            steps {
                // 컨테이너 간 통신을 위한 Docker 네트워크 생성 (이미 존재하면 무시)
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
                                        echo "--- Printing environment variables for debugging ---"
                                        sh """docker run --rm --name ${env.BACKEND_CONTAINER_NAME}_debug \
                                            -e MSSQL_SERVER=${MSSQL_SERVER} \
                                            -e MSSQL_DATABASE=${MSSQL_DATABASE} \
                                            -e MSSQL_USER=${MSSQL_USER} \
                                            -e MSSQL_PASSWORD=${MSSQL_PASSWORD} \
                                            -e MSSQL_PORT=${MSSQL_PORT} \
                                            ${env.BACKEND_IMAGE_NAME} printenv"""

                                        echo "--- Reverting to original docker run command ---"
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
                                    docker.build(env.FRONTEND_IMAGE_NAME, './frontend')
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
            // 파이프라인 실행 후 항상 Jenkins 작업 공간을 정리합니다.
            echo "Cleaning up workspace..."
            cleanWs()
        }
    }
}

// 컨테이너를 안전하게 중지하고 삭제하는 헬퍼 함수
def dockerStopRemove(containerName) {
    // sh 명령어는 Linux 기반 컨테이너 환경에서 실행됩니다.
    sh "docker stop ${containerName} || true"
    sh "docker rm ${containerName} || true"
}
