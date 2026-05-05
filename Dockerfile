# --- Frontend ---
FROM node:20-alpine AS frontend-builder
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm install

COPY frontend/ ./
RUN npm run build

# --- Backend ---
FROM gradle:jdk24 AS backend-builder
WORKDIR /app/backend
COPY backend/ ./
RUN mkdir -p src/main/resources/static

COPY --from=frontend-builder /app/frontend/dist/foobs/browser/ ./src/main/resources/static

RUN gradle build -x test --no-daemon

FROM eclipse-temurin:24-jre-alpine
WORKDIR /app
COPY --from=backend-builder /app/backend/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]