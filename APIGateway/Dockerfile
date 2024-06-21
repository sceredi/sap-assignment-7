FROM gradle:8.7.0-jdk17

WORKDIR /app

COPY build.gradle.kts .

COPY gradle gradle
COPY gradlew .

COPY src src

EXPOSE 8080

CMD ["gradle", "bootRun"]

HEALTHCHECK --interval=30s --retries=3 CMD curl -f http://localhost:8080/health
