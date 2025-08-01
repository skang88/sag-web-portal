import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

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
        target: 'http://backend:5000',
        changeOrigin: true,
        // Flask 백엔드가 '/api/data' 경로를 그대로 기대하므로
        // 경로를 재작성(rewrite)할 필요가 없습니다.
      }
    }
  }
})
