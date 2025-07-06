# Stage 1: Build phase
FROM openjdk:17-slim as BUILD_PHASE
RUN apt-get update && apt-get install -y dos2unix && apt-get upgrade -y && apt-get dist-upgrade -y
WORKDIR /usr/src/app
COPY . .
RUN dos2unix ./mvnw
RUN chmod +x ./mvnw
RUN ./mvnw -DskipTests clean package

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine as RUNTIME
WORKDIR /app
COPY --from=BUILD_PHASE /usr/src/app/target/movierama-0.0.1-SNAPSHOT.jar /app/app.jar

# Set default ingestion period to 10s (10000ms), override with INGESTION_PERIOD env

ENTRYPOINT ["java", "-jar", "app.jar"]