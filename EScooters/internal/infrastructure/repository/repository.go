package repository

import "github.com/sceredi/sap-assignment-5/escooters-service/internal/core/domain"

type Storer interface {
	RegisterEScooter(e domain.EScooter) error
}

type Retriever interface {
	GetEScooter(id string) (*domain.EScooter, error)
}
