FROM openjdk:8-jdk-alpine

WORKDIR /app

RUN apk add --no-cache curl

COPY gradlew .
COPY gradle/ ./gradle/

COPY build.gradle.kts .
COPY settings.gradle.kts .

COPY ./ .

RUN ./gradlew build

EXPOSE 8080

CMD ["./gradlew", "run"]

HEALTHCHECK --interval=30s --retries=5 CMD curl -f http://localhost:8080/users/health

