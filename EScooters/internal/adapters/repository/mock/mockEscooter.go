package mock

import "github.com/sceredi/sap-assignment-5/escooters-service/internal/core/domain"

type Answer struct {
	*domain.EScooter
	Error error
}

type MockDB struct {
	RegisterAnswers []Answer
	GetAnswers      []Answer
}

var registerCount = 0
var getCount = 0

func (e *MockDB) RegisterEScooter(id string) (*domain.EScooter, error) {
	if registerCount >= len(e.RegisterAnswers) {
		ans := e.RegisterAnswers[len(e.RegisterAnswers)-1]
		return ans.EScooter, ans.Error
	}
	ans := e.RegisterAnswers[registerCount]
	registerCount++
	return ans.EScooter, ans.Error
}

func (e *MockDB) GetEScooter(id string) (*domain.EScooter, error) {
	if getCount >= len(e.RegisterAnswers) {
		ans := e.GetAnswers[len(e.RegisterAnswers)-1]
		return ans.EScooter, ans.Error
	}
	ans := e.GetAnswers[getCount]
	getCount++
	return ans.EScooter, ans.Error
}
