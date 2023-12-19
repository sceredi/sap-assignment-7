FROM openjdk:8-jdk-alpine

WORKDIR /app

RUN apk add --no-cache curl

COPY gradlew .
COPY gradle gradle

COPY build.gradle.kts .
COPY settings.gradle.kts .

COPY src .

RUN ./gradlew build

EXPOSE 8080

CMD ["./gradlew", "run"]

