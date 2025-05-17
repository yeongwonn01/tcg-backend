FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# 이미 빌드된 JAR 파일만 복사
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]