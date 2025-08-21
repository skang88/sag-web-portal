import base64
import cv2
import threading
from flask_socketio import SocketIO

# Global thread for the video stream
thread = None
thread_lock = threading.Lock()

# RTSP URL from the original relay.js
RTSP_URL = 'rtsp://admin:1q2w3e4r@172.16.222.44:554'

def video_stream_thread(socketio: SocketIO):
    """
    Reads frames from the RTSP stream, encodes them, and emits them to clients.
    """
    print(f"Attempting to connect to RTSP stream: {RTSP_URL}")
    cap = cv2.VideoCapture(RTSP_URL)

    if not cap.isOpened():
        print("Error: Could not open RTSP stream.")
        return

    print("RTSP stream opened successfully.")
    while True:
        # Yield control to other greenlets to allow for WebSocket communication
        socketio.sleep(0.03) # Approx 30 FPS
        
        ret, frame = cap.read()
        if not ret:
            print("Error: Failed to grab frame. Reconnecting...")
            cap.release()
            cap = cv2.VideoCapture(RTSP_URL)
            if not cap.isOpened():
                socketio.sleep(1) # Wait a second before retrying
                continue
            continue

        # Encode the frame as JPEG
        # Encode the frame as JPEG with lower quality for faster transmission
        _, buffer = cv2.imencode('.jpg', frame, [cv2.IMWRITE_JPEG_QUALITY, 50])
        
        # Convert the buffer to a base64 string
        jpg_as_text = base64.b64encode(buffer).decode('utf-8')

        # Broadcast the frame to all connected clients
        socketio.emit('video_frame', {'image': jpg_as_text})

    cap.release()
    print("RTSP stream thread stopped.")

def register_socketio_events(socketio: SocketIO):
    @socketio.on('connect')
    def connect():
        """
        Handles a new client connection. Starts the background video thread if it's not running.
        """
        global thread
        print('Client connected')
        with thread_lock:
            if thread is None:
                thread = socketio.start_background_task(target=video_stream_thread, socketio=socketio)
                print('Started video stream thread.')

    @socketio.on('disconnect')
    def disconnect():
        print('Client disconnected')
