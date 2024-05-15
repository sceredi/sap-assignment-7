package services

import (
	"github.com/sceredi/sap-assignment-5/escooters-service/internal/core/domain"
	"github.com/sceredi/sap-assignment-5/escooters-service/internal/core/ports"
)

// EScootersService is a service that provides methods to interact with the EScooter domain.
type EScootersService struct {
	repo ports.EScooterRepository
}

// NewEScootersService creates a new EScootersService with the given repository.
func NewEScootersService(repo ports.EScooterRepository) *EScootersService {
	return &EScootersService{
		repo: repo,
	}
}

// RegisterEScooter registers a new EScooter with the given id.
func (s *EScootersService) RegisterEScooter(id string) (*domain.EScooter, error) {
	return s.repo.RegisterEScooter(id)
}

// GetEScooter returns the EScooter with the given id.
func (s *EScootersService) GetEScooter(id string) (*domain.EScooter, error) {
	return s.repo.GetEScooter(id)
}
