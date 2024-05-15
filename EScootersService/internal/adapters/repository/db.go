package repository

import (
	"context"

	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

// DB represents the collection on the mongo database
type DB struct {
	Client     *mongo.Client
	Collection *mongo.Collection
}

// NewDB creates the connection with the database at named name, and the given
// collection, returns the DB and a bool, the bool is true if the connection is
// ready, false otherwise
func NewDB(connectionString, name, collection string) (*DB, bool) {
	clentOptions := options.Client().ApplyURI(connectionString)
	client, err := mongo.Connect(context.TODO(), clentOptions)
	if err != nil || !ping(client) {
		return nil, false
	}
	dbCollection := client.Database(name).Collection(collection)
	db := &DB{
		Client:     client,
		Collection: dbCollection,
	}
	return db, true
}

func ping(client *mongo.Client) bool {
	if err := client.Ping(context.TODO(), nil); err != nil {
		return false
	}
	return true
}
