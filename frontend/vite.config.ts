import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// Docker 환경에서 실행 중인지 확인하기 위한 환경 변수
const isDocker = process.env.DOCKER_DEV === 'true'

// 백엔드 API의 주소를 환경에 따라 동적으로 설정합니다.
const backendTarget = isDocker ? 'http://backend:5001' : 'http://localhost:5001'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    proxy: {
      // '/api'로 시작하는 모든 요청을 백엔드 컨테이너로 전달합니다.
      '/api': {
        target: backendTarget,
        changeOrigin: true,
        // Flask 백엔드가 '/api/data' 경로를 그대로 기대하므로
        // 경로를 재작성(rewrite)할 필요가 없습니다.
      }
    }
  }
})
