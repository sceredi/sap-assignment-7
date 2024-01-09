FROM golang:1.21

# Set destination for COPY
WORKDIR /app

# Download Go modules
COPY go.mod go.sum ./
RUN go mod download

COPY ./ ./

# Build
RUN go build -o api-gateway ./app

EXPOSE 8080

# Run
CMD ["./api-gateway"]
