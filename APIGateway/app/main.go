package main

import (
	"api-gateway/routes"
	"fmt"
)

func main() {
	fmt.Print("Api Gateway")
	config, err := routes.LoadRoutesConfig("routes.yaml")
	if err != nil {
		fmt.Println("Error loading configuration:", err)
		return
	}
	fmt.Printf("config: %v\n", config)
}
