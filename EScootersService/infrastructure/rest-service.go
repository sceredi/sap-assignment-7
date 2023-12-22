package infrastructure

import (
	"escooters/service/domain"
	"log"

	"github.com/labstack/echo/v4"
)

type RestService struct {
	server *echo.Echo
	service domain.EScootersService
}

type Result struct {
	Result   string          `json:"result"`
	EScooter domain.EScooter `json:"escooter"`
}

func NewRestService(service domain.EScootersService) *RestService {
	return &RestService{
		server: echo.New(),
		service: service,
	}
}

func (r *RestService) Start() {
	log.Println("Starting escooter microservice...")
	r.registerRoutes()
	r.server.Start(":8080")
}

func (r *RestService) Stop() {
	log.Println("Closing rest server")
	r.server.Close()
}

func (r *RestService) registerRoutes() {
	r.server.POST("/escooters", r.registerNewEScooter)
	r.server.GET("/escooters/:id", r.getEscooter)
}

func (r *RestService) registerNewEScooter(c echo.Context) error {
	log.Println("Registering new escooter")
	escooter := new(domain.EScooter)
	if err := c.Bind(escooter); err != nil {
		return err
	}
	if escooter.Id == "" {
		return c.String(400, "id is required")
	}
	escooter, err := r.service.RegisterNewEScooter(*escooter)
	if err != nil {
		return c.String(500, err.Error())
	}
	return c.JSON(200, Result{Result: "ok", EScooter: *escooter})
}

func (r *RestService) getEscooter(c echo.Context) error {
	id := c.Param("id")
	if id == "" {
		return c.String(400, "id is required")
	}
	log.Println("Getting escooter with id: " + id)
	escooter, err := r.service.GetEScooter(id)
	if err != nil {
		return c.String(500, err.Error())
	}
	if escooter == nil {
		return c.String(404, "escooter not found")
	}
	return c.JSON(200, Result{Result: "ok", EScooter: *escooter})
}
