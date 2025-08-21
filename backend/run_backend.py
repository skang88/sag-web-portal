import eventlet
eventlet.monkey_patch()

# Import the Flask app instance from backend/app.py
from .app import socketio, app

if __name__ == '__main__':
    print("Flask-SocketIO server is starting...")
    socketio.run(app, port=5001, host='0.0.0.0', use_reloader=False)
