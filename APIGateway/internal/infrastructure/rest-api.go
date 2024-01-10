package infrastructure

import (
	"errors"
	"fmt"
	"log"
	"net/http"
	"net/http/httputil"
	"net/url"
	"strings"

	routes "api-gateway/internal/domain/routes"

	"github.com/labstack/echo/v4"
)

func Start(config *routes.Config) error {
	e := echo.New()
	fmt.Println("Starting API Gateway")

	for _, route := range config.Routes {
		fmt.Printf("route: %v\n", route)
		handler:= reverseProxy(route.Target)
		switch route.Method {
		case "GET":
			e.GET(route.Source, handler)
		case "POST":
			e.POST(route.Source, handler)
		case "PUT":
			e.PUT(route.Source, handler)
		case "DELETE":
			e.DELETE(route.Source, handler)
		default:
			return errors.New("Invalid method")
		}
	}
	e.Start(":8080")
	return nil
}

func reverseProxy(target string) echo.HandlerFunc {
	return func(c echo.Context) error {
		targetURL, err := url.Parse(target)
		if err != nil {
			return c.String(http.StatusInternalServerError, "Error parsing target URL")
		}

		proxy := httputil.NewSingleHostReverseProxy(targetURL)
		c.Request().Host = targetURL.Host
		fmt.Printf("c.Request().Body: %v\n", c.Request().Body)
		fmt.Printf("c.Request().URL.Path: %v\n", c.Request().URL.Path)
		c.Request().URL.Path = strings.Join(c.ParamValues(), "/")
		fmt.Printf("c.Request().URL.Path: %v\n", c.Request().URL.Path)
		fmt.Printf("c.Request().URL.Host: %v\n", c.Request().URL.Host)
		log.Println("Is this working?")
		proxy.ServeHTTP(c.Response(), c.Request())
		log.Println("Is this still working?")

		return nil
	}
}
