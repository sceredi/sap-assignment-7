package main

import (
	"fmt"
	"net/http"
	"os"

	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
)

func main() {
	fmt.Println("EScooters Frontend")
	e := echo.New()
	e.Use(middleware.Logger())
	e.Use(middleware.Recover())
	e.Use(middleware.CORS())
	e.Static("/static", "assets")
	e.GET("/health", func(c echo.Context) error {
		return c.String(http.StatusOK, "Healthy")
	})
	e.POST("/kill", func(c echo.Context) error {
		os.Exit(1)
		return c.String(http.StatusOK, "Kill Confirmed")
	})
	e.Start(":3000")
}
