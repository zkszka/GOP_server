# Stage 1: Build
FROM openjdk:17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew bootJar

# Stage 2: Runtime
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/build/libs/GOP_server-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
