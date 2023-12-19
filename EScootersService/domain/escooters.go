package EScooters

type EScooter struct {
	ID string `bson:"_id,omitempty"`
	Id int    `bson:"id"`
}

func New(Id int) *EScooter {
	return &EScooter{Id: Id}
}
