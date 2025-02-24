# Base 이미지 설정
FROM openjdk:17
WORKDIR /app

# Gradle 빌드 후 생성된 JAR 파일을 복사
COPY build/libs/*.jar app.jar

# 컨테이너가 실행될 때 Spring Boot 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
