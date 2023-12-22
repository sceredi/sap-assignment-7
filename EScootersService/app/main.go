package main

import (
	"escooters/service/application"
	"escooters/service/domain"
	"escooters/service/infrastructure"
	"log"
	"os"

	"github.com/joho/godotenv"
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
	domainmodel := domain.NewEScootersModel(repository)
	escooterService := application.NewEScooterService(domainmodel)
	restService := infrastructure.NewRestService(escooterService)
	restService.Start()
	defer func() {
		log.Println("Closing connection to MongoDB")
		repository.Close()
		log.Println("Closing rest server")
		restService.Stop()
	}()
}
