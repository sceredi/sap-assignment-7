package webservice

import (
	"log"
	"net/http"

	"github.com/sceredi/sap-assignment-5/escooters-service/internal/adapters/handler"
	"github.com/sceredi/sap-assignment-5/escooters-service/internal/middleware"
)

// Starts the webserver on the given address
func Serve(addr string, handler *handler.EScootersHandler) {
	router := http.NewServeMux()
	loadHandlers(router, handler)
	stack := middleware.CreateStack(
		middleware.Logging,
	)
	server := http.Server{
		Addr:    addr,
		Handler: stack(router),
	}
	log.Printf("Server listening on %s\n", server.Addr)
	server.ListenAndServe()
}
