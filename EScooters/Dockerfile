FROM golang:1.22.2

# Set destination for COPY
WORKDIR /app

# Download Go modules
COPY go.mod go.sum ./
RUN go mod download

COPY ./cmd ./cmd
COPY ./internal ./internal

# Build
RUN go build -o escooters-service ./cmd

EXPOSE 8080

# Run
CMD ["./escooters-service"]

