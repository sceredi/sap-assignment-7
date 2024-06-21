FROM gradle:8.7.0-jdk21

WORKDIR /app

COPY build.gradle.kts .
COPY gradle.properties .

COPY gradle gradle
COPY gradlew .

COPY src src

RUN gradle build

EXPOSE 8080

CMD ["gradle", "run"]

HEALTHCHECK --interval=30s --retries=3 CMD curl -f http://localhost:8080/health

