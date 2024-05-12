package repository

import (
	"context"
	"log"

	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

// DB represents the collection on the mongo database
type DB struct {
	collection *mongo.Collection
}

// NewMongoClient creates a new connection to the mongo database listening
// at connectionString e.g "localhost:27017"
func NewMongoClient(connectionString string) (*mongo.Client, error) {
	clentOptions := options.Client().ApplyURI(connectionString)
	client, err := mongo.Connect(context.TODO(), clentOptions)
	if err != nil {
		return nil, err
	}
	defer func() {
		if err = client.Disconnect(context.TODO()); err != nil {
			log.Panic(err)
		}
	}()
	return client, nil
}

// NewDB creates the connection with the database at named name, and the given
// collection
func NewDB(client *mongo.Client, name, collection string) (*DB, error) {
	dbCollection := client.Database(name).Collection(collection)
	db := &DB{
		collection: dbCollection,
	}
	return db, nil
}
