package domain


type EScootersModelImpl struct {
	repository EScootersRepository
}

func NewEScootersModel(repo EScootersRepository) *EScootersModelImpl{
	return &EScootersModelImpl{repository: repo}
}

func (e *EScootersModelImpl) AddNewEScooter(escooter EScooter) (*EScooter, error) {
	if err := e.repository.RegisterNewEScooter(escooter); err != nil {
		return nil, err
	}
	return &escooter, nil
}

func (e *EScootersModelImpl) GetEScooter(id string) (*EScooter, error) {
	return e.repository.GetEScooter(id)
}
