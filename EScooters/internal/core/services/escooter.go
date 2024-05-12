package services

import (
	"github.com/sceredi/sap-assignment-5/escooters-service/internal/core/domain"
	"github.com/sceredi/sap-assignment-5/escooters-service/internal/core/ports"
)

type EScootersService struct {
	repo ports.EScooterRepository
}

func (s *EScootersService) RegisterEScooter(id string) (*domain.EScooter, error) {
	return s.repo.RegisterEScooter(id)
}

func (s *EScootersService) GetEScooter(id string) (*domain.EScooter, error) {
	return s.repo.GetEScooter(id)
}
