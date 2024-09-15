# Stage 1: Build
FROM openjdk:17-slim AS build

WORKDIR /app

COPY . .

# 필수 패키지 설치 (alpine 기반 이미지는 apk 사용)
RUN apt update && apt install -y wget unzip

# Gradle Wrapper 다운로드 및 설치
RUN wget https://services.gradle.org/distributions/gradle-8.8-bin.zip -P /tmp \
    && unzip /tmp/gradle-8.8-bin.zip -d /opt \
    && ln -s /opt/gradle-8.8/bin/gradle /usr/bin/gradle

# Gradle로 빌드
RUN ./gradlew bootJar

# Stage 2: Runtime
FROM openjdk:17-slim

WORKDIR /app

COPY --from=build /app/build/libs/GOP_server-0.0.1-SNAPSHOT.jar /app/GOP_server.jar

# 9977 포트 노출
EXPOSE 9977

ENTRYPOINT ["java", "-jar", "/app/GOP_server.jar"]
