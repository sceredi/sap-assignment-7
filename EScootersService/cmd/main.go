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
	if err := godotenv.Load(); err != nil {
		log.Println("No .env file found")
	}
	port := os.Getenv("PORT")
	if port == "" {
		port = "8080"
	}
	addr := fmt.Sprintf(":%s", port)
	mongoAddr := os.Getenv("MONGO_URI")
	db_name := "escooters_db"
	collection := "escooters"
	repo, connOk := repository.NewDB(mongoAddr, db_name, collection)
	if !connOk {
		log.Panic("Cannot get escooters collection\n")
	}
	defer func() {
		if err := repo.Client.Disconnect(context.TODO()); err != nil {
			log.Panic(err)
		}
	}()
	log.Printf("Connected to MongoDB at %s\n", mongoAddr)
	log.Printf("Collection %s now available\n", collection)
	escootersService := services.NewEScootersService(repo)
	handler := handler.NewEScootersHandler(escootersService)

	webservice.Serve(addr, handler)
}
