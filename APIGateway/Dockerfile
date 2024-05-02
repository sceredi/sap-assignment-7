FROM gradle:8.7.0

WORKDIR /app

# RUN apt-get update && apt-get install findutils -y

COPY ./ .

# RUN ./gradlew assemble

EXPOSE 8080

CMD ["gradle", "bootRun"]


