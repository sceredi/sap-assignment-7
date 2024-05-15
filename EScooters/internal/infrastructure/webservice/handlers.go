package webservice

import (
	"log"
	"net/http"

	"github.com/sceredi/sap-assignment-5/escooters-service/internal/adapters/handler"
)

// Loads the handlers for all the possible requests
func loadHandlers(router *http.ServeMux, handler *handler.EScootersHandler) {
	router.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(200)
	})

	router.HandleFunc("POST /escooters", handler.RegisterEScooter)

	router.HandleFunc("GET /escooters/{id}", func(w http.ResponseWriter, r *http.Request) {
		id := r.PathValue("id")
		if id == "" {
			_, err := w.Write([]byte("Error: the id must be not empty"))
			if err != nil {
				log.Printf("Error sending message to the client:\n%s\n", err)
				return
			}
		}
		_, err := w.Write([]byte("Nice you gave me the id"))
		if err != nil {
			log.Printf("Error sending message to the client:\n%s\n", err)
			return
		}
	})
}
