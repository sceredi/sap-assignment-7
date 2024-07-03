package main

import (
	"net/http"
	"os"

	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

// Create a new Prometheus counter
var (
	requestsTotal = prometheus.NewCounter(prometheus.CounterOpts{
		Name: "requests_total",
		Help: "The total number of received requests",
	})
)

func init() {
	// Register the counter with Prometheus
	prometheus.MustRegister(requestsTotal)
}

// Middleware to count requests
func requestCounter(next echo.HandlerFunc) echo.HandlerFunc {
	return func(c echo.Context) error {
		requestsTotal.Inc()
		return next(c)
	}
}

func main() {
	e := echo.New()
	e.Use(middleware.Logger())
	e.Use(middleware.Recover())
	e.Use(middleware.CORS())
	e.Use(requestCounter)
	e.Static("/static", "assets")
	e.GET("/health", func(c echo.Context) error {
		return c.String(http.StatusOK, "Healthy")
	})
	e.POST("/kill", func(c echo.Context) error {
		os.Exit(1)
		return c.String(http.StatusOK, "Kill Confirmed")
	})
	// Route to expose Prometheus metrics
	e.GET("/metrics", echo.WrapHandler(promhttp.Handler()))
	e.Logger.Fatal(e.Start(":3000"))
}
