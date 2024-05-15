package main

import (
	"context"
	"fmt"
	"log"
	"os"

	"github.com/joho/godotenv"
	"github.com/sceredi/sap-assignment-5/escooters-service/internal/adapters/handler"
	"github.com/sceredi/sap-assignment-5/escooters-service/internal/adapters/repository"
	"github.com/sceredi/sap-assignment-5/escooters-service/internal/core/services"
	"github.com/sceredi/sap-assignment-5/escooters-service/internal/infrastructure/webservice"
)

func main() {
	log.Println("Hello World!")
	if err := godotenv.Load(); err != nil {
		log.Println("No .env file found")
	}
	port := os.Getenv("PORT")
	addr := fmt.Sprintf(":%s", port)
	mongoAddr := os.Getenv("MONGO_URI")
	db_name := "escooters_db"
	collection := "escooters"
	repo, err := repository.NewDB(mongoAddr, db_name, collection)
	log.Printf("Connected to MongoDB at %s\n", mongoAddr)
	defer func() {
		if err = repo.Client.Disconnect(context.TODO()); err != nil {
			log.Panic(err)
		}
	}()

	if err != nil {
		log.Panicf("Cannot get escooters collection\n%s\n", err)
	}
	log.Printf("Collection %s now available\n", collection)
	escootersService := services.NewEScootersService(repo)
	handler := handler.NewEScootersHandler(escootersService)

	webservice.Serve(addr, handler)
}
