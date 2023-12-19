package main

import (
	"fmt"
    "EScootersService/domain"
)

func main() {
    escooter:=EScooters.New(1)
    fmt.Println(escooter.Id)
}
