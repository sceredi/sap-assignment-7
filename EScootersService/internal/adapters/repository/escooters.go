package repository

import (
	"context"
	"errors"
	"fmt"
	"log"

	"github.com/sceredi/sap-assignment-5/escooters-service/internal/core/domain"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"
)

// RegisterEScooter creates a new escooter instance on the database
func (e *DB) RegisterEScooter(id string) (*domain.EScooter, error) {
	escooter := &domain.EScooter{
		Id: id,
	}
	log.Printf("Registering a new escooter with id %s\n", escooter.Id)
	result, err := e.Collection.InsertOne(context.TODO(), *escooter)
	if err != nil {
		if mongo.IsDuplicateKeyError(err) {
			return nil, errors.New(fmt.Sprintf(domain.ErrorDuplicateEScooter, escooter.Id))
		}
		return nil, err
	}
	log.Printf("Registered new escooter with _id %v\n", result.InsertedID)
	return escooter, nil
}

// GetEScooter gets the EScooter with the given id from the database
func (e *DB) GetEScooter(id string) (*domain.EScooter, error) {
	log.Printf("Getting escooter with id: %s\n", id)
	result := e.Collection.FindOne(context.TODO(), bson.M{"_id": id})
	var escooter domain.EScooter
	if err := result.Decode(&escooter); err != nil {
		if err == mongo.ErrNoDocuments {
			return nil, errors.New(fmt.Sprintf(domain.ErrorEScooterNotFound, id))
		}
		return nil, err
	}
	log.Printf("Got escooter with id: %s\n", escooter.Id)
	return &escooter, nil

}
