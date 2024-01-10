package main

import (
	"fmt"

	Routes "api-gateway/internal/domain/routes"
	Server "api-gateway/internal/infrastructure"
)

func main() {
	fmt.Print("Api Gateway")
	config, err := Routes.LoadConfig("routes.yaml")
	if err != nil {
		fmt.Println("Error loading configuration:", err)
		return
	}
	fmt.Printf("config: %v\n", config)

	err = Server.Start(config)
	if err != nil {
		panic(err)
	}
}
