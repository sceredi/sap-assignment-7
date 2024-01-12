FROM openjdk

WORKDIR /app

COPY ./ .

RUN ./gradlew build

EXPOSE 8080

CMD ["./gradlew", "run"]


