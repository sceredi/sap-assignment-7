package infrastructure

import (
	EScooters "escooters/service/domain"
	"log"

	"github.com/labstack/echo/v4"
)

type RestService struct {
	server *echo.Echo
}

func NewRestService() *RestService {
	return &RestService{
		server: echo.New(),
	}
}

func (r *RestService) Start() {
	log.Println("Starting escooter microservice...")
	r.registerRoutes()
	r.server.Start(":8080")
}

func (r *RestService) registerRoutes() {
	r.server.POST("/escooter", createEscooter)
	r.server.GET("/escooter/:id", getEscooter)
}

func createEscooter(c echo.Context) error {
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

	panic("not implemented")
}
