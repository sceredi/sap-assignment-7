package repository

import (
	"context"
	"log"

	"github.com/sceredi/sap-assignment-5/escooters-service/internal/core/domain"
)

func (e *DB) RegisterEScooter(id string) (*domain.EScooter, error) {
	escooter := domain.EScooter{
		Id: id,
	}
	log.Printf("Registering a new escooter with id %s\n", escooter.Id)
	result, err := e.collection.InsertOne(context.TODO(), escooter)
	if err != nil {
		return nil, err
	}
	log.Printf("Registered new escooter with _id %v\n", result.InsertedID)
	return &escooter, nil
}
