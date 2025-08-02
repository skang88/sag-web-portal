# SAG Web Portal - Backend

이 프로젝트는 Flask로 구현된 SAG Web Portal의 백엔드 서버입니다.

## 시작하기

이 안내서는 프로젝트를 로컬 컴퓨터에 설정하고 실행하는 방법을 설명합니다.

### 사전 준비

- Python 3.x
- `venv` (Python 설치 시 기본 포함)

### 설치 및 설정

1.  **백엔드 디렉토리로 이동합니다.**
    ```bash
    cd path/to/your/project/sag-web-portal/backend
    ```

2.  **가상 환경(venv)을 생성하고 활성화합니다.**

    아직 가상 환경을 만들지 않았다면, 아래 명령어로 생성합니다.
    ```bash
    python -m venv venv
    ```

    다음 명령어로 가상 환경을 활성화합니다.

    -   **Windows (Command Prompt):**
        ```bash
        venv\Scripts\activate.bat
        ```
    -   **Windows (PowerShell):**
        ```powershell
        .\venv\Scripts\Activate.ps1
        ```
    -   **macOS / Linux:**
        ```bash
        source venv/bin/activate
        ```
    (터미널 프롬프트 앞에 `(venv)`가 보이면 성공적으로 활성화된 것입니다.)

3.  **의존성 패키지를 설치합니다.**
    가상 환경이 활성화된 상태에서 `requirements.txt` 파일을 사용하여 모든 의존성 패키지를 한 번에 설치합니다.
    ```bash
    pip install -r requirements.txt
    ```

### 개발 서버 실행

1.  `backend` 디렉토리에서 가상 환경이 활성화되었는지 확인합니다.
2.  아래 명령어로 개발 서버를 시작합니다.
    ```bash
    python app.py
    ```
3.  서버는 `http://127.0.0.1:5001` 에서 실행됩니다.

### API 엔드포인트

- **`GET /api/data`**: "Hello from Flask Backend!" 메시지가 담긴 JSON을 반환합니다.

## 의존성 관리

프로젝트에 새로운 패키지를 추가하거나 기존 패키지를 업데이트한 경우, 다음 명령어를 사용하여 `requirements.txt` 파일을 최신 상태로 유지하세요.

```bash
pip freeze > requirements.txt
```
