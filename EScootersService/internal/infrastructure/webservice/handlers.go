package webservice

import (
	"net/http"

	"github.com/sceredi/sap-assignment-5/escooters-service/internal/adapters/handler"
)

// Loads the handlers for all the possible requests
func loadHandlers(router *http.ServeMux, handler *handler.EScootersHandler) {
	router.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(200)
	})

	router.HandleFunc("POST /escooters", handler.RegisterEScooter)

	router.HandleFunc("GET /escooters/{id}", handler.GetEScooter)

	router.HandleFunc("POST /kill", handler.Kill)

	router.HandleFunc("GET /metrics", handler.Metrics)

	router.HandleFunc("/", handler.NotFound)
}
