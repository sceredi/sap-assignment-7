package webservice

import (
	"net/http"

	"github.com/sceredi/sap-assignment-5/escooters-service/internal/adapters/handler"
	"github.com/sceredi/sap-assignment-5/escooters-service/internal/middleware"
	log "github.com/sirupsen/logrus"
)

// Starts the webserver on the given address
func Serve(addr string, handler *handler.EScootersHandler) {
	router := http.NewServeMux()
	loadHandlers(router, handler)
	stack := middleware.CreateStack(
		middleware.Logging,
		middleware.UpdateMetric,
	)
	server := http.Server{
		Addr:    addr,
		Handler: stack(router),
	}
	log.Infof("Server listening on %s\n", server.Addr)
	server.ListenAndServe()
}
