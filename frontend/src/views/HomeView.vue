<template>
  <div class="home">
    <h1>Welcome to the SAG Web Portal</h1>
    <p>This is the home page.</p>

    <div class="api-test mt-5">
      <h2>Backend API Test</h2>
      <p v-if="error" class="text-danger">Error: {{ error }}</p>
      <div v-else-if="apiData">
        <p>Data from backend: {{ apiData }}</p>
      </div>
      <p v-else>Loading data from backend...</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';

const apiData = ref<string | null>(null);
const error = ref<string | null>(null);

onMounted(async () => {
  try {
    // Use the VITE_API_BACKEND_URL environment variable
    const backendUrl = import.meta.env.VITE_API_BACKEND_URL || 'http://localhost:5001'; // Fallback for local dev
    const response = await fetch(`${backendUrl}/api/data`);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    apiData.value = JSON.stringify(data); // Stringify for display
  } catch (e: any) {
    error.value = e.message;
    console.error('Failed to fetch API data:', e);
  }
});
</script>

<style scoped>
.home {
  text-align: center;
  padding: 50px;
}

.api-test {
  margin-top: 30px;
  border-top: 1px solid #eee;
  padding-top: 20px;
}
</style>
