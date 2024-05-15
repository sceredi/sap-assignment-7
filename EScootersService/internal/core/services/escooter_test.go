package services_test

import (
	"errors"
	"fmt"
	"testing"

	"github.com/sceredi/sap-assignment-5/escooters-service/internal/adapters/repository/mock"
	"github.com/sceredi/sap-assignment-5/escooters-service/internal/core/domain"
	"github.com/sceredi/sap-assignment-5/escooters-service/internal/core/services"
)

var (
	goodEScooter1 = &domain.EScooter{
		Id: "1",
	}
	goodEScooter2 = &domain.EScooter{
		Id: "3",
	}
	registerAnswers = []mock.Answer{
		{
			EScooter: goodEScooter1,
			Error:    nil,
		},
		{
			EScooter: nil,
			Error:    errors.New(fmt.Sprintf(domain.ErrorDuplicateEScooter, "2")),
		},
		{
			EScooter: goodEScooter2,
			Error:    nil,
		},
		{
			EScooter: nil,
			Error:    errors.New(fmt.Sprintf(domain.ErrorDuplicateEScooter, "4")),
		},
	}
	getAnswers = []mock.Answer{
		{
			EScooter: goodEScooter1,
			Error:    nil,
		},
		{
			EScooter: nil,
			Error:    errors.New(fmt.Sprintf(domain.ErrorEScooterNotFound, "2")),
		},
		{
			EScooter: goodEScooter2,
			Error:    nil,
		},
		{
			EScooter: nil,
			Error:    errors.New(fmt.Sprintf(domain.ErrorEScooterNotFound, "4")),
		},
	}
	DBMock = &mock.MockDB{
		RegisterAnswers: registerAnswers,
		GetAnswers:      getAnswers,
	}
)

func TestRegisterEScooter(t *testing.T) {
	service := services.NewEScootersService(DBMock)
	result, err := service.RegisterEScooter(goodEScooter1.Id)
	if result.Id != goodEScooter1.Id || err != nil {
		t.Errorf("RegisterEScooter, expected %v; got %v\n", *goodEScooter1, *result)
	}
	result, err = service.RegisterEScooter(goodEScooter1.Id)
	if result != nil || err.Error() != fmt.Sprintf(domain.ErrorDuplicateEScooter, "2") {
		t.Errorf("Should have gotten an error; got %s", err)
	}
}

func TestGetEScooter(t *testing.T) {
	service := services.NewEScootersService(DBMock)
	result, err := service.GetEScooter(goodEScooter1.Id)
	if result.Id != goodEScooter1.Id || err != nil {
		t.Errorf("GetEScooter, expected %v; got %v\n", *goodEScooter1, *result)
	}
	result, err = service.GetEScooter(goodEScooter1.Id)
	if result != nil || err.Error() != fmt.Sprintf(domain.ErrorEScooterNotFound, "2") {
		t.Errorf("Should have gotten an error; got %s", err)
	}
}
