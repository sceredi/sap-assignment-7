package EScooters


type EScooters struct {
    Id int `json:"id"`
}

func New(Id int) *EScooters {
    return &EScooters{Id: Id}
}
