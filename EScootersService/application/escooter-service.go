package application

import (
	"escooters/service/domain"
	"log"
)

type EScooterServiceImpl struct {
	model domain.EScootersModel
}

func NewEScooterService(model domain.EScootersModel) *EScooterServiceImpl {
	return &EScooterServiceImpl{model: model}
}

func (e *EScooterServiceImpl) RegisterNewEScooter(escooter domain.EScooter) (*domain.EScooter, error) {
	log.Println("Registering new escooter")
	return e.model.AddNewEScooter(escooter)
}

func (e *EScooterServiceImpl) GetEScooter(id string) (*domain.EScooter, error) {
	log.Println("Getting escooter with id: " + id)
	return e.model.GetEScooter(id)
}
