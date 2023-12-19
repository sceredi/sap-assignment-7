FROM golang:1.21

# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Download dependencies using go modules
RUN go mod download

# Build the Go app
RUN go build -o api-gateway

# Expose port 8080 to the outside world
EXPOSE 8080

# Command to run the executable
CMD ["./rides-service"]

