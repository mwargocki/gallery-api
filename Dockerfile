# Etap 1: Build stage z Gradle + JDK 21
FROM gradle:8.6-jdk21 AS builder
COPY . /app
WORKDIR /app
RUN ./gradlew build --no-daemon

# Etap 2: Run stage z lekkim JRE 21
FROM eclipse-temurin:21-jre
COPY --from=builder /app/build/libs/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]