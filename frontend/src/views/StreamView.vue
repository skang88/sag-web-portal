<template>
  <div class="stream-container">
    <h1>RTSP Video Stream</h1>
    <div id="video-container">
      <img id="video" :src="videoSrc" alt="Video stream is loading...">
    </div>
    <p id="status">{{ status }}</p>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { io, type Socket } from 'socket.io-client';

const videoSrc = ref('');
const status = ref('Connecting to server...');
let socket: Socket | null = null;

onMounted(() => {
  // Connect to the Socket.IO server on the Flask backend
  socket = io('http://localhost:5001');

  socket.on('connect', () => {
    console.log('Connected to WebSocket server.');
    status.value = 'Connection established. Waiting for video...';
  });

  socket.on('video_frame', (data: { image: string }) => {
    // Update the image source with the new frame data
    videoSrc.value = `data:image/jpeg;base64,${data.image}`;
    if (status.value !== '') {
        status.value = ''; // Hide status once video starts
    }
  });

  socket.on('disconnect', () => {
    console.log('Disconnected from WebSocket server.');
    status.value = 'Disconnected from server. Please refresh.';
  });

  socket.on('connect_error', (err) => {
    console.error('Connection Error:', err);
    status.value = 'Failed to connect to the server.';
  });
});

onUnmounted(() => {
  if (socket) {
    socket.disconnect();
  }
});
</script>

<style scoped>
.stream-container {
  font-family: Arial, sans-serif;
  background-color: #2c2c2c;
  color: #f0f0f0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
}
h1 {
  margin-bottom: 20px;
}
#video-container {
  border: 2px solid #555;
  background-color: #000;
  min-width: 640px;
  min-height: 480px;
  display: flex;
  align-items: center;
  justify-content: center;
}
#video {
  width: 100%;
  height: auto;
  display: block;
}
#status {
  margin-top: 15px;
  font-style: italic;
  color: #aaa;
}
</style>
