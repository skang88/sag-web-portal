<script setup>
import { ref, onMounted } from 'vue'

// 백엔드로부터 받은 메시지를 저장할 반응형 변수
const apiMessage = ref('Loading data from backend...')
const asnApiResult = ref('Click the button to test the ASN API.')

// 컴포넌트가 마운트될 때 백엔드 API를 호출하는 함수
onMounted(async () => {
  try {
    // 백엔드로부터 데이터를 수신중이다는 메시지를 표시하기 위해 0.5초의 지연 시간 추가 
    await new Promise(resolve => setTimeout(resolve, 700));
    
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

async function testAsnApi() {
  asnApiResult.value = 'Fetching ASN data...'
  try {
    const response = await fetch('https://seohanga.com/api/asn?date=20250801&group=01')
    if (!response.ok) {
      throw new Error(`Network response was not ok: ${response.statusText}`)
    }
    const data = await response.json()
    asnApiResult.value = JSON.stringify(data, null, 2)
  } catch (error) {
    console.error('Failed to fetch ASN data:', error)
    asnApiResult.value = `Error: Could not retrieve data. ${error.message}`
  }
}
</script>

<template>
  <main class="container py-5 d-flex flex-column align-items-center justify-content-center vh-100">
    <div class="text-center mb-5">
      <h1 class="display-4 fw-bold text-dark">Welcome to Seohan Auto Georgia!!</h1>
    </div>

    <div class="card shadow p-4" style="width: 100%; max-width: 600px;">
      <div class="text-center card-body">
        <h2 class="card-title h4 mb-3 text-primary">Flask Backend API Test Result:</h2>
        <p class="card-text border p-3 rounded bg-light fw-bold text-success">{{ apiMessage }}</p>
      </div>
    </div>

    <div class="card shadow p-4 mt-4" style="width: 100%; max-width: 600px;">
        <div class="text-center card-body">
            <h2 class="card-title h4 mb-3 text-primary">Express Backend API Test Result:</h2>
            <button @click="testAsnApi" class="btn btn-secondary mb-3">Test ASN API</button>
            <pre class="card-text border p-3 rounded bg-light text-start" style="white-space: pre-wrap; max-height: 300px; overflow-y: auto;"><code>{{ asnApiResult }}</code></pre>
        </div>
    </div>
  </main>
</template>