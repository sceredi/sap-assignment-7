package main

import (
	Routes "api-gateway/internal/domain/routes"
	"fmt"
)

func main() {
	fmt.Print("Api Gateway")
	config, err := Routes.LoadConfig("routes.yaml")
	if err != nil {
		fmt.Println("Error loading configuration:", err)
		return
	}
	fmt.Printf("config: %v\n", config)
}
