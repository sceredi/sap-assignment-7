package infrastructure

import (
	"context"
	"escooters/service/domain"

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
	client, err := mongo.Connect(context.TODO(), clentOptions)
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
	r.client.Disconnect(context.TODO())
}

func (r *Repository) GetEScooter(id string) (*domain.EScooter, error) {
	log.Println("Getting escooter with id: " + id)
	result := r.collection.FindOne(context.TODO(), bson.M{"_id": id})
	log.Println("got something")
	var escooter *domain.EScooter
	if err := result.Decode(escooter); err != nil {
		if err == mongo.ErrNoDocuments {
			return nil, nil
		}
		return nil, err
	}
	log.Println(escooter)
	return escooter, nil
}

func (r *Repository) RegisterNewEScooter(escooter domain.EScooter) error {
	log.Println("Registering new escooter with id: " + escooter.Id)
	_, err := r.collection.InsertOne(context.TODO(), escooter)
	return err
}
