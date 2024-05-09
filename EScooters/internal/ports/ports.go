package ports

import "github.com/sceredi/sap-assignment-5/escooters-service/internal/domain"

type EScootersService interface {
	RegisterEScooter(e domain.EScooter) error
	GetEScooter(id string) (*domain.EScooter, error)
}

type EScooterRepository interface {
	RegisterEScooter(e domain.EScooter) error
	GetEScooter(id string) (*domain.EScooter, error)
}
