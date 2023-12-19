package main

import (
	repository "escooters/service/infrastructure"
	"log"
	"os"

	"github.com/joho/godotenv"
	"go.mongodb.org/mongo-driver/mongo"
)

func main() {
	if err := godotenv.Load(); err != nil {
		log.Println("Error loading .env file")
	}
	connectionString := os.Getenv("MONGO_URI")
	if connectionString == "" {
		log.Fatal("MONGO_URI environment variable is not set")
	}
	escootersRepository, err := repository.NewRepository(connectionString)
	if err != nil {
		log.Fatal(err)
		log.Fatal("Error connecting to MongoDB")
		panic(err)
	}
	defer escootersRepository.Close()
	escooter, err := escootersRepository.GetEScooter("1")
	if err == mongo.ErrNoDocuments {
		log.Println("No escooters found")
		return
	}
	if err != nil {
		panic(err)
	}
	if escooter == nil {
		log.Println("No escooters found")
		return
	}

	log.Println(escooter)
}
