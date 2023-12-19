FROM golang:1.19

# Set destination for COPY
WORKDIR /app

# Download Go modules
COPY go.mod go.sum ./
RUN go mod download

COPY *.go ./

# Build
RUN go build -o /api-gateway

EXPOSE 8080

# Run
CMD ["/api-gateway"]
