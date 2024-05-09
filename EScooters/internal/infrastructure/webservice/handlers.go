package webservice

import (
	"log"
	"net/http"
)

func loadHandlers(router *http.ServeMux) {
	router.HandleFunc("POST /escooters", func(w http.ResponseWriter, r *http.Request) {
		_, err := w.Write([]byte("Got a new POST request"))
		if err != nil {
			log.Printf("Error sending message to the client:\n%s\n", err)
		}
	})
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
