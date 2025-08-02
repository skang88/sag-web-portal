# 1. 베이스 이미지 선택 (Python 3.11 슬림 버전)
FROM python:3.11-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 환경 변수 설정 (버퍼링 없이 즉시 로그 출력)
ENV PYTHONUNBUFFERED 1

# 4. 의존성 설치
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 5. 애플리케이션 코드 복사
COPY . .

# 6. 포트 노출 및 서버 실행
EXPOSE 5001
CMD ["gunicorn", "--bind", "0.0.0.0:5001", "app:app"]