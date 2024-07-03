package main

import (
	"context"
	"fmt"
	"os"

	"github.com/joho/godotenv"
	"github.com/sceredi/sap-assignment-5/escooters-service/internal/adapters/handler"
	"github.com/sceredi/sap-assignment-5/escooters-service/internal/adapters/repository"
	"github.com/sceredi/sap-assignment-5/escooters-service/internal/core/services"
	"github.com/sceredi/sap-assignment-5/escooters-service/internal/infrastructure/webservice"
	log "github.com/sirupsen/logrus"
)

func main() {
	log.SetFormatter(&log.JSONFormatter{})

	if err := godotenv.Load(); err != nil {
		log.Info("No .env file found")
	}

	logFilePath := os.Getenv("LOG_FILE_PATH")
	if logFilePath == "" {
		logFilePath = "./escooters_service.log"
	}

	logFile, err := os.OpenFile(logFilePath, os.O_CREATE|os.O_WRONLY|os.O_APPEND, 0666)
	if err != nil {
		log.Errorf("Failed to log to file, using default stderr: %v", err)
	}
	log.SetOutput(logFile)

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
		log.Fatal("Cannot get escooters collection\n")
	}
	defer func() {
		if err := repo.Client.Disconnect(context.TODO()); err != nil {
			log.Fatal(err)
		}
	}()
	log.Infof("Connected to MongoDB at %s\n", mongoAddr)
	log.Infof("Collection %s now available\n", collection)
	escootersService := services.NewEScootersService(repo)
	handler := handler.NewEScootersHandler(escootersService)

	webservice.Serve(addr, handler)
}
