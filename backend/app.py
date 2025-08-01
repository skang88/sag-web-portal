# /path/to/your/project/my-web-portal/backend/app.py

from flask import Flask, jsonify
from flask_cors import CORS
from dotenv import load_dotenv
import os

# .env 파일에서 환경 변수 로드
load_dotenv()

# 환경 변수 테스트
test_var = os.getenv("TEST_VAR")
print(f"TEST_VAR from .env: {test_var}")

# Flask 애플리케이션 생성
app = Flask(__name__)

# CORS 설정 (모든 도메인에서 오는 요청을 허용)
# 개발 중에는 이렇게 간단히 설정하고, 프로덕션에서는 특정 도메인만 허용하도록 변경하는 것이 좋습니다.
CORS(app)

# 기본 API 엔드포인트
@app.route('/api/data')
def get_data():
    # 간단한 JSON 데이터 반환
    return jsonify({
        "message": "Hello from Flask Backend!"
    })



if __name__ == '__main__':
    # 디버그 모드로 서버 실행 (개발 중에만 사용)
    app.run(debug=True)
