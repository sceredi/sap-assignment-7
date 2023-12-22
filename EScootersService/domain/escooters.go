package domain

type EScooter struct {
	Id string `bson:"_id" json:"id"`
}

type EScootersModel interface {
	AddNewEScooter(escooter EScooter) (*EScooter, error)
	GetEScooter(id string) (*EScooter, error)
}

type EScootersRepository interface {
	RegisterNewEScooter(escooter EScooter) error
	GetEScooter(id string) (*EScooter, error)
}

type EScootersService interface {
	RegisterNewEScooter(escooter EScooter) (*EScooter, error)
	GetEScooter(id string) (*EScooter, error)
}
