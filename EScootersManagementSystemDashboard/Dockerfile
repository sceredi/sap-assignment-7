FROM golang:1.22.2

# Set destination for COPY
WORKDIR /app

# Download Go modules
COPY go.mod go.sum ./
RUN go mod download

COPY *.go ./
COPY ./assets ./assets

# Build
RUN go build -o /dashboard

EXPOSE 3000

# Run
CMD ["/dashboard"]
