# /path/to/your/project/my-web-portal/backend/app.py

from flask import Flask, jsonify, request
from flask_cors import CORS
from dotenv import load_dotenv
import os
import pyodbc

# .env 파일에서 환경 변수 로드
load_dotenv()

# Flask 애플리케이션 생성
app = Flask(__name__)

# CORS 설정
CORS(app)

def get_db_connection():
    server = os.getenv('MSSQL_SERVER')
    database = os.getenv('MSSQL_DATABASE')
    username = os.getenv('MSSQL_USER')
    password = os.getenv('MSSQL_PASSWORD')
    port = os.getenv('MSSQL_PORT', 1433)
    driver = '{ODBC Driver 18 for SQL Server}' # '{SQL Server}'

    # For debugging: Print environment variables (do not do this in production with sensitive data)
    print(f"DEBUG: MSSQL_SERVER={server}")
    print(f"DEBUG: MSSQL_DATABASE={database}")
    print(f"DEBUG: MSSQL_USER={username}")
    print(f"DEBUG: MSSQL_PASSWORD={password}") # Mask password
    print(f"DEBUG: MSSQL_PORT={port}")
    print(f"DEBUG: MSSQL_PORT={driver}")
    
    conn_str = f'DRIVER={driver};SERVER={server},{port};DATABASE={database};UID={username};PWD={password};Encrypt=yes;TrustServerCertificate=yes'
    
    try:
        conn = pyodbc.connect(conn_str)
        return conn
    except pyodbc.Error as ex:
        sqlstate = ex.args[0]
        print(f"Database connection failed: {sqlstate}")
        return None

# 기본 API 엔드포인트
@app.route('/api/data')
def get_data():
    return jsonify({"message": "Hello from Flask Backend!"})

@app.route('/api/asn', methods=['GET'])
def get_asn():
    date = request.args.get('date')
    group = request.args.get('group')

    if not date or not group:
        return jsonify({"error": "Date and group parameters are required."}), 400

    conn = get_db_connection()
    if conn is None:
        return jsonify({"error": "Failed to connect to the database."}), 500

    cursor = conn.cursor()

    sql_query = '''
      SELECT 
        LEFT(A.LOCAT, 8) AS date, 
        RIGHT(A.LOCAT, 2) AS shippingGroup,
        B.SPEC_TX AS palletSerial,
        CASE 
          WHEN RIGHT(A.ITMNO, 1) = 'K' THEN LEFT(A.ITMNO, LEN(A.ITMNO) - 1)
          ELSE A.ITMNO
        END AS partNumber, -- <- K 제거
        mapping.SHORT_NAME AS description,
        A.QTY AS deliveryQty, 
        'EA' AS unit, 
        '5500003006' AS poNumber,
        mapping.ORD AS poItem,
        'RETURNABLE' AS packaging
      FROM MAT_LOCA_ALM A
      INNER JOIN MAT_BARCODE_HIS B ON A.BIGO01 = B.QR_NO
      INNER JOIN dbo.MAT_ITMMAPPING mapping ON A.ITMNO = mapping.ITMNO
      WHERE A.GUBN = 'B' 
        AND LEFT(A.LOCAT, 8) = ?
        AND RIGHT(A.LOCAT, 2) = ?
        AND A.STS <> 'D'
      ORDER BY A.ITMNO, A.BIGO02;
    '''

    try:
        cursor.execute(sql_query, date, group)
        rows = cursor.fetchall()
        columns = [column[0] for column in cursor.description]
        
        results = [dict(zip(columns, row)) for row in rows]

        return jsonify({
            "count": len(results),
            "data": results
        })

    except pyodbc.Error as err:
        print(f"Database query failed: {err}")
        return jsonify({"error": "Database query failed"}), 500
    finally:
        conn.close()


if __name__ == '__main__':
    app.run()
