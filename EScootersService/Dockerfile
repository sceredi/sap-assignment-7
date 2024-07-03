FROM golang:1.22.2

# Set destination for COPY
WORKDIR /app

# Download Go modules
COPY go.mod go.sum ./
RUN go mod download

COPY . .

# Build
RUN go build -o escooters-service ./cmd

EXPOSE 8080

# Run
CMD ["./escooters-service"]

HEALTHCHECK --interval=30s --retries=3 CMD curl -f http://localhost:8080/health
