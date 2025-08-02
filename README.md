# SAG Web Portal

Vue.js와 Flask를 사용하여 구축된 웹 포털 프로젝트입니다. 전체 애플리케이션은 일관된 개발 및 배포 환경을 위해 Docker를 사용하여 컨테이너화되었습니다.

## ✨ 기술 스택

*   **Frontend**: [Vue.js](https://vuejs.org/) (with Vite)
*   **Backend**: [Flask](https://flask.palletsprojects.com/) (with Gunicorn)
*   **Web Server (Frontend)**: [Nginx](https://www.nginx.com/)
*   **Containerization**: [Docker](https://www.docker.com/) & [Docker Compose](https://docs.docker.com/compose/)
*   **CI/CD**: [Jenkins](https://www.jenkins.io/)

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
    *   **Backend API**: `http://localhost:8050/api/data` 에서 API 엔드포인트를 테스트할 수 있습니다. Nginx 웹 서버가 `/api`로 시작하는 요청을 백엔드 컨테이너(포트 5001)로 전달(proxy)합니다.
    
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

이 프로젝트는 Jenkins를 사용하여 CI(지속적 통합) 파이프라인을 구축합니다. `Jenkinsfile`은 파이프라인을 코드로 정의하며, Git에 코드가 푸시될 때마다 자동으로 애플리케이션을 빌드하고 로컬 환경에 배포합니다.

### 파이프라인 단계

1.  **Checkout:** Git 저장소에서 최신 코드를 가져옵니다.
2.  **Prepare Environment:** 컨테이너 간 통신을 위한 Docker 네트워크(`sag-portal-net`)를 생성합니다.
3.  **Build & Deploy:**
    *   `backend`와 `frontend` Docker 이미지를 병렬로 빌드합니다.
    *   기존 컨테이너가 실행 중인 경우 중지하고 삭제합니다.
    *   새로운 버전의 컨테이너를 실행하여 애플리케이션을 배포합니다.

### Jenkins 파이프라인 설정 방법

`Jenkinsfile`을 사용하여 Jenkins에 파이프라인을 등록하는 방법은 다음과 같습니다.

1.  Jenkins 대시보드에서 **"New Item"**을 클릭합니다.
2.  아이템 이름을 입력하고(예: `sag-web-portal-pipeline`), **"Pipeline"** 유형을 선택한 후 "OK"를 클릭합니다.
3.  **"Pipeline"** 탭으로 스크롤하여 다음을 설정합니다.
    *   **Definition:** 드롭다운 메뉴에서 **"Pipeline script from SCM"**을 선택합니다.
    *   **SCM:** **"Git"**을 선택합니다.
    *   **Repository URL:** 이 프로젝트의 Git 저장소 URL을 입력합니다. (예: `https://github.com/skang88/sag-web-portal.git`)
    *   **Script Path:** `Jenkinsfile` (기본값이므로 대부분 변경할 필요가 없습니다.)
4.  **"Save"**를 클릭하여 파이프라인 설정을 저장합니다.
5.  이제 **"Build Now"**를 클릭하여 파이프라인을 실행할 수 있습니다.