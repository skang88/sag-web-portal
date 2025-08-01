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
    docker-compose up --build
    ```

3.  **애플리케이션 접속:**
    *   **Frontend**: 브라우저를 열고 `http://localhost:8080` 으로 접속하세요.
    *   **Backend API**: `http://localhost:5001/api/data` 에서 API 엔드포인트를 테스트할 수 있습니다.