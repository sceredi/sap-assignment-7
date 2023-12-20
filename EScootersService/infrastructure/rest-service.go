package infrastructure

import (
	EScooters "escooters/service/domain"
	"log"

	"github.com/labstack/echo/v4"
)

type RestService struct {
	server *echo.Echo
}

type Result struct {
	Result string `json:"result"`
	EScooter EScooters.EScooter `json:"escooter"`
}

var repository *Repository

func NewRestService() *RestService {
	return &RestService{
		server: echo.New(),
	}
}

func (r *RestService) Start(repo *Repository) {
	log.Println("Starting escooter microservice...")
	repository = repo
	r.registerRoutes()
	r.server.Start(":8080")
}

func (r *RestService) Stop() {
	log.Println("Closing rest server")
	r.server.Close()
}

func (r *RestService) registerRoutes() {
	r.server.POST("/escooters", registerNewEScooter)
	r.server.GET("/escooters/:id", getEscooter)
}

func registerNewEScooter(c echo.Context) error {
	escooter := new(EScooters.EScooter)
	if err := c.Bind(escooter); err != nil {
		return err
	}
	panic("not implemented")
}

func getEscooter(c echo.Context) error {
	id := c.Param("id")
	if id == "" {
		return c.String(400, "id is required")
	}
	log.Println("Getting escooter with id: " + id)
	escooter, err := repository.GetEScooter(id)
	if err != nil {
		return c.String(500, err.Error())
	}
	if escooter == nil {
		return c.String(404, "escooter not found")
	}
	return c.JSON(200, Result{Result: "ok", EScooter: *escooter})
}
