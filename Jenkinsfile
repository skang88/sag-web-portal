pipeline {
    // 모든 Jenkins 에이전트에서 실행 가능 (단, Docker가 설치되어 있어야 함)
    agent any

    environment {
        // Docker Hub 사용자 이름 또는 다른 레지스트리 주소로 변경하세요.
        DOCKER_REGISTRY = 'skang88'
        BACKEND_IMAGE_NAME = "${DOCKER_REGISTRY}/sag-portal-backend"
        FRONTEND_IMAGE_NAME = "${DOCKER_REGISTRY}/sag-portal-frontend"
        // Jenkins 빌드 번호를 이미지 태그로 사용하거나 'latest'를 사용합니다.
        IMAGE_TAG = "build-${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                // Git 저장소에서 코드를 가져옵니다.
                checkout scm
            }
        }

        stage('Build Docker Images') {
            parallel {
                stage('Build Backend') {
                    steps {
                        script {
                            echo "Building Backend Docker image: ${BACKEND_IMAGE_NAME}:${IMAGE_TAG}"
                            docker.build("${BACKEND_IMAGE_NAME}:${IMAGE_TAG}", './backend')
                        }
                    }
                }
                stage('Build Frontend') {
                    steps {
                        script {
                            echo "Building Frontend Docker image: ${FRONTEND_IMAGE_NAME}:${IMAGE_TAG}"
                            docker.build("${FRONTEND_IMAGE_NAME}:${IMAGE_TAG}", './frontend')
                        }
                    }
                }
            }
        }

        stage('Login to Docker Registry') {
            steps {
                echo "Logging in to Docker Hub..."
                // Jenkins에 저장된 Docker Hub 인증 정보의 Credentials ID를 사용합니다.
                withCredentials([usernamePassword(credentialsId: 'DOCKER_HUB_CREDENTIALS', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                script {
                    echo "Pushing Backend image..."
                    docker.image("${BACKEND_IMAGE_NAME}:${IMAGE_TAG}").push()

                    echo "Pushing Frontend image..."
                    docker.image("${FRONTEND_IMAGE_NAME}:${IMAGE_TAG}").push()
                }
            }
        }

        stage('Deploy to Server') {
            steps {
                // 이 단계는 배포 환경에 따라 달라집니다.
                // 보통 sshagent를 사용하여 원격 서버에 접속하고 docker-compose를 실행합니다.
                echo "배포 스크립트를 여기에 추가하세요."
                // 예시: sshagent(['your-server-ssh-key']) {
                //   sh 'ssh user@your.server.com "cd /home/user/sag-web-portal && docker-compose pull && docker-compose up -d"'
                // }
            }
        }
    }

    post {
        always {
            // 파이프라인 실행 후 항상 실행되는 정리 단계
            echo "Cleaning up..."
            sh "docker rmi ${BACKEND_IMAGE_NAME}:${IMAGE_TAG} || true"
            sh "docker rmi ${FRONTEND_IMAGE_NAME}:${IMAGE_TAG} || true"
            sh 'docker logout'
        }
    }
}