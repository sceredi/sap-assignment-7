package main

import (
	infrastructure "escooters/service/infrastructure"
	"github.com/joho/godotenv"
	"log"
	"os"
)

func main() {
	log.Println("Starting escooter microservice...")
	if err := godotenv.Load(); err != nil {
		log.Println("No .env file found")
	}
	connectionString := os.Getenv("MONGO_URI")
	repository, err := infrastructure.NewRepository(connectionString)
	if err != nil {
		log.Fatal(err)
	}
	restService := infrastructure.NewRestService()
	restService.Start(repository)
	defer func() {
		log.Println("Closing connection to MongoDB")
		repository.Close()
		log.Println("Closing rest server")
		restService.Stop()
	}()
}
