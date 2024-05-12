package ports

import "github.com/sceredi/sap-assignment-5/escooters-service/internal/core/domain"

type EScootersService interface {
	RegisterEScooter(id string) (*domain.EScooter, error)
	GetEScooter(id string) (*domain.EScooter, error)
}

type EScooterRepository interface {
	RegisterEScooter(id string) (*domain.EScooter, error)
	GetEScooter(id string) (*domain.EScooter, error)
}
