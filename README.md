# SAG Web Portal

Vue.js와 Flask를 사용하여 구축된 웹 포털 프로젝트입니다. 전체 애플리케이션은 일관된 개발 및 배포 환경을 위해 Docker를 사용하여 컨테이너화되었습니다.

## ✨ 기술 스택

*   **Frontend**: [Vue.js](https://vuejs.org/) (with Vite)
*   **Backend**: [Flask](https://flask.palletsprojects.com/) (with Gunicorn)
*   **Web Server (Frontend)**: [Nginx](https://www.nginx.com/)
*   **Containerization**: [Docker](https://www.docker.com/) & [Docker Compose](https://docs.docker.com/compose/)

## 📂 프로젝트 구조

이 프로젝트는 프론트엔드와 백엔드 코드를 단일 저장소에서 관리하기 위해 모노레포(Monorepo) 구조를 따릅니다.

```
.
├── backend/      # Flask 애플리케이션
├── frontend/     # Vue.js 애플리케이션
├── docker-compose.yml
├── Jenkinsfile # Jenkins 파이프라인 정의
└── README.md
```

## 🚀 시작하기

### 사전 준비

컴퓨터에 다음 소프트웨어가 설치되어 있는지 확인하세요.
*   [Docker](https://www.docker.com/get-started)
*   [Docker Compose](https://docs.docker.com/compose/install/) (일반적으로 Docker Desktop에 포함되어 있습니다)

### 설치 및 실행

1.  **저장소 클론:**
    ```bash
    git clone https://github.com/skang88/sag-web-portal.git
    cd sag-web-portal
    ```

2.  **컨테이너 빌드 및 실행:**
    Docker Compose를 사용하여 이미지를 빌드하고 서비스를 시작합니다.
    ```bash
    docker-compose up --build -d
    ```

3.  **애플리케이션 접속:**
    *   **Frontend**: 브라우저를 열고 `http://localhost:8050` 으로 접속하세요.
    *   **Backend API**: `http://localhost:5001/api/data` 에서 API 엔드포인트를 테스트할 수 있습니다.
    
4.  **컨테이너 중지:**
    애플리케이션을 중지하고 컨테이너를 삭제하려면 다음 명령을 실행하세요.
    ```bash
    docker-compose down
    ```

## ⚙️ 환경 변수

이 프로젝트는 `docker-compose.yml`에서 환경 변수를 사용할 수 있습니다. 원활한 실행을 위해 프로젝트 루트에 `.env` 파일을 생성하고 필요한 변수를 설정하는 것을 권장합니다.

```env
# .env.example
FLASK_ENV=development
```

## 🚀 CI/CD

이 프로젝트는 Jenkins를 사용하여 CI/CD 파이프라인을 구축합니다. `Jenkinsfile`은 파이프라인을 코드로 정의하며, 주요 단계는 다음과 같습니다:

1.  **Checkout:** Git 저장소에서 최신 코드를 가져옵니다.
2.  **Backend Build & Push:** `backend` Docker 이미지를 빌드하고 Docker Hub와 같은 컨테이너 레지스트리에 푸시합니다.
3.  **Frontend Build & Push:** `frontend` Docker 이미지를 빌드하고 컨테이너 레지스트리에 푸시합니다.
4.  **Deploy:** 대상 서버에서 `docker-compose pull` 및 `docker-compose up -d` 명령을 실행하여 최신 버전의 애플리케이션을 배포합니다.