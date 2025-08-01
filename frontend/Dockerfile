# ---- 1. Build Stage ----
# Vue 프로젝트를 빌드하는 단계
FROM node:20 as build-stage

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

# ---- 2. Serve Stage ----
# 빌드된 정적 파일을 Nginx로 서비스하는 단계
FROM nginx:stable-alpine

# Build Stage에서 생성된 dist 폴더의 내용물을 Nginx의 기본 웹 루트로 복사
COPY --from=build-stage /app/dist /usr/share/nginx/html

# 커스텀 Nginx 설정을 복사
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]