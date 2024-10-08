# Stage 1: Build
FROM openjdk:17-slim AS build

WORKDIR /app

# 필요한 파일 복사
COPY gradlew ./
COPY gradle gradle/
COPY build.gradle ./
COPY settings.gradle ./
COPY src src/

# 필수 패키지 설치
RUN apt update && apt install -y wget unzip

# Gradle Wrapper 다운로드 및 설치
RUN wget https://services.gradle.org/distributions/gradle-8.8-bin.zip -P /tmp \
    && unzip /tmp/gradle-8.8-bin.zip -d /opt \
    && ln -s /opt/gradle-8.8/bin/gradle /usr/bin/gradle

# gradlew에 실행 권한 부여
RUN chmod +x ./gradlew

# Gradle로 빌드
RUN ./gradlew bootJar

# Stage 2: Runtime
FROM openjdk:17-slim

WORKDIR /app

# JAR 파일 복사 (명시적으로 파일 이름 지정)
COPY --from=build /app/build/libs/GOP_server-0.0.1-SNAPSHOT.jar /app/GOP_server.jar

EXPOSE 9977

# ENTRYPOINT 수정: JAR 파일 경로를 명시적으로 지정
ENTRYPOINT ["java", "-jar", "/app/GOP_server.jar"]
