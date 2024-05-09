package webservice

import (
	"net/http"

	"github.com/sceredi/sap-assignment-5/escooters-service/internal/middleware"
)

// Starts the webserver on the given address
func Serve(addr string) {
	router := http.NewServeMux()
	loadHandlers(router)
	stack := middleware.CreateStack(
		middleware.Logging,
	)
	server := http.Server{
		Addr:    addr,
		Handler: stack(router),
	}
	server.ListenAndServe()
}
