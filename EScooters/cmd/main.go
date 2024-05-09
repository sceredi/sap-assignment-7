package main

import (
	"fmt"
	"log"
	"os"

	"github.com/joho/godotenv"
	"github.com/sceredi/sap-assignment-5/escooters-service/internal/infrastructure/webservice"
)

func main() {
	log.Println("Hello World!")
	if err := godotenv.Load(); err != nil {
		log.Println("No .env file found")
	}
	port := os.Getenv("PORT")
	addr := fmt.Sprintf(":%s", port)

	webservice.Serve(addr)
}
