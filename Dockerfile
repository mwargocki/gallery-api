# Etap 1: Build stage z JDK 21
FROM gradle:8.6-jdk21 AS builder
COPY . /app
WORKDIR /app
RUN ./gradlew build -x test --no-daemon

# Etap 2: Run stage z lekkim JRE 21
FROM eclipse-temurin:21-jre
COPY --from=builder /app/build/libs/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]