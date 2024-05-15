package repository

import (
	"context"
	"log"

	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

// DB represents the collection on the mongo database
type DB struct {
	Client     *mongo.Client
	Collection *mongo.Collection
}

// NewDB creates the connection with the database at named name, and the given
// collection
func NewDB(connectionString, name, collection string) (*DB, error) {
	clentOptions := options.Client().ApplyURI(connectionString)
	client, err := mongo.Connect(context.TODO(), clentOptions)
	if err != nil || !ping(client) {
		return nil, err
	}
	dbCollection := client.Database(name).Collection(collection)
	db := &DB{
		Client:     client,
		Collection: dbCollection,
	}
	return db, nil
}

func ping(client *mongo.Client) bool {
	if err := client.Ping(context.TODO(), nil); err != nil {
		log.Println("Database not connected")
		return false
	}
	return true
}
