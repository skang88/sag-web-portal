# 1. 베이스 이미지 선택 (Python 3.11 슬림 버전)
FROM python:3.11-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 환경 변수 설정 (버퍼링 없이 즉시 로그 출력)
ENV PYTHONUNBUFFERED 1
ENV PYTHONPATH=/app

# 4. Microsoft ODBC Driver for SQL Server 및 네트워크 도구 설치
RUN apt-get update && apt-get install -y curl gnupg netcat-traditional libgl1 libxrender1 libglib2.0-0
RUN curl -fsSL https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor -o /usr/share/keyrings/microsoft-prod.gpg
RUN curl -fsSL https://packages.microsoft.com/config/debian/12/prod.list > /etc/apt/sources.list.d/mssql-release.list

RUN apt-get update
RUN ACCEPT_EULA=Y apt-get install -y msodbcsql18 unixodbc-dev

# 5. 의존성 설치
COPY requirements.txt /app/backend/requirements.txt
RUN pip install --no-cache-dir -r /app/backend/requirements.txt

# 6. 애플리케이션 코드 복사
COPY . /app/backend

# 7. 포트 노출 및 서버 실행
EXPOSE 5001
CMD ["python", "-m", "backend.run_backend"]