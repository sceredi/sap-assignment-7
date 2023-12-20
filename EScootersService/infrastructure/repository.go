package infrastructure

import (
	"context"
	domain "escooters/service/domain"
	"log"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

type Repository struct {
	client     *mongo.Client
	collection *mongo.Collection
}

func NewRepository(connectionString string) (*Repository, error) {
	clentOptions := options.Client().ApplyURI(connectionString)
	client, err := mongo.Connect(context.Background(), clentOptions)
	if err != nil {
		return nil, err
	}
	log.Println("Connected to MongoDB")
	collection := client.Database("escooters_db").Collection("escooters")
	log.Println(collection.Name())
	return &Repository{client: client, collection: collection}, nil
}

func (r *Repository) Close() {
	log.Println("Closing connection to MongoDB")
	r.client.Disconnect(context.Background())
}

func (r *Repository) GetEScooter(id string) (*domain.EScooter, error) {
	log.Println("Getting escooter with id: " + id)
	result := r.collection.FindOne(context.Background(), bson.M{"id": id})
	log.Println("got something")
	var escooter domain.EScooter
	if err := result.Decode(&escooter); err != nil {
		if err == mongo.ErrNoDocuments {
			return nil, nil
		}
		return nil, err
	}
	log.Println(escooter)
	return &escooter, nil
}
