FROM gradle:8.7.0-jdk17

WORKDIR /app

COPY build.gradle.kts .
COPY gradle.properties .

COPY gradle gradle
COPY gradlew .

RUN gradle build

COPY src src

EXPOSE 8080

CMD ["gradle", "run"]

HEALTHCHECK --interval=30s --retries=5 CMD curl -f http://localhost:8080/users/health

