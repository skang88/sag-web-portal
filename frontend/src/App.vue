<script setup>
import { ref, onMounted } from 'vue'

// 백엔드로부터 받은 메시지를 저장할 반응형 변수
const apiMessage = ref('Loading data from backend...')

// 컴포넌트가 마운트될 때 백엔드 API를 호출하는 함수
onMounted(async () => {
  try {
    // Nginx 프록시를 통해 백엔드 API 호출
    const response = await fetch('/api/data')
    if (!response.ok) {
      throw new Error(`Network response was not ok: ${response.statusText}`)
    }
    const data = await response.json()
    apiMessage.value = data.message // 응답 메시지로 업데이트
  } catch (error) {
    console.error('Failed to fetch data:', error)
    apiMessage.value = 'Error: Could not retrieve data from backend.'
  }
})
</script>

<template>
  <main>
    <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="125" height="125" />

    <div class="welcome-message">
      <h1>Welcome to Seohan Auto Georgia</h1>
    </div>

    <div class="api-test-result">
      <h2>Backend API Test Result:</h2>
      <p class="message-box">{{ apiMessage }}</p>
    </div>
  </main>
</template>

<style scoped>
main {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 80vh;
  font-family: Avenir, Helvetica, Arial, sans-serif;
  text-align: center;
}

.logo {
  margin-bottom: 2rem;
}

.welcome-message h1 {
  font-size: 2.5rem;
  color: #2c3e50;
  margin-bottom: 2rem;
}

.api-test-result {
  margin-top: 2rem;
}

.api-test-result h2 {
  color: #34495e;
}

.message-box {
  margin-top: 1rem;
  padding: 1rem 2rem;
  border: 1px solid #42b983;
  border-radius: 8px;
  background-color: #f0f9f5;
  color: #1a5e42;
  font-weight: bold;
  font-size: 1.2rem;
}
</style>

