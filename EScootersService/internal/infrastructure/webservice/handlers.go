package webservice

import (
	"net/http"

	"github.com/sceredi/sap-assignment-5/escooters-service/internal/adapters/handler"
	"github.com/sceredi/sap-assignment-5/escooters-service/internal/adapters/repository"
)

// Loads the handlers for all the possible requests
func loadHandlers(router *http.ServeMux, handler *handler.EScootersHandler, db *repository.DB) {
	router.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		if !db.Ping() {
			w.WriteHeader(http.StatusServiceUnavailable)
			return
		}
		w.WriteHeader(http.StatusOK)
	})

	router.HandleFunc("POST /escooters", handler.RegisterEScooter)

	router.HandleFunc("GET /escooters/{id}", handler.GetEScooter)

	router.HandleFunc("POST /kill", handler.Kill)

	router.HandleFunc("GET /metrics", handler.Metrics)

	router.HandleFunc("/", handler.NotFound)
}
