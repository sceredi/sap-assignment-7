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
	"github.com/sirupsen/logrus"
)

func main() {
	logrus.SetFormatter(&logrus.JSONFormatter{})

	if err := godotenv.Load(); err != nil {
		log.Println("No .env file found")
	}

	logFilePath := os.Getenv("LOG_FILE_PATH")
	if logFilePath == "" {
		logFilePath = "./escooters_service.log"
	}

	logFile, err := os.OpenFile(logFilePath, os.O_CREATE|os.O_WRONLY|os.O_APPEND, 0666)
	if err != nil {
		logrus.Fatalf("Failed to log to file, using default stderr: %v", err)
	}
	logrus.SetOutput(logFile)
	logrus.Info("Hello this is a log\n")

	port := os.Getenv("PORT")
	if port == "" {
		// default port if not specified differently
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
