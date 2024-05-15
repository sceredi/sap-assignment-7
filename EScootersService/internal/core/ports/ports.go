package ports

import "github.com/sceredi/sap-assignment-5/escooters-service/internal/core/domain"

// EScootersService is the interface that provides the methods to interact with the EScooter domain.
type EScootersService interface {
	RegisterEScooter(id string) (*domain.EScooter, error)
	GetEScooter(id string) (*domain.EScooter, error)
}

// EScooterRepository is the interface that provides the methods to interact with the EScooter storage.
type EScooterRepository interface {
	RegisterEScooter(id string) (*domain.EScooter, error)
	GetEScooter(id string) (*domain.EScooter, error)
}
