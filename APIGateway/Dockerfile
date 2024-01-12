FROM openjdk:20-jdk-slim

WORKDIR /app

RUN apt-get update && apt-get install findutils -y

COPY ./ .

# RUN ./gradlew assemble

EXPOSE 8080

CMD ["./gradlew", "bootRun"]


