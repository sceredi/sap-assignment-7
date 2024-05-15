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

	// router.HandleFunc("POST /escooters", func(w http.ResponseWriter, r *http.Request) {
	// 	type RequestBody struct {
	// 		EscooterId string `json:"escooterId"`
	// 	}
	// 	dec := json.NewDecoder(r.Body)
	// 	dec.DisallowUnknownFields()
	// 	var rb RequestBody
	// 	err := dec.Decode(&rb)
	// 	if err != nil {
	// 		http.Error(w, err.Error(), http.StatusBadRequest)
	// 		return
	// 	}
	// 	log.Printf("GOT THE ID --> %s\n", rb.EscooterId)
	//
	// 	_, err = w.Write([]byte("Got a new POST request"))
	// 	if err != nil {
	// 		log.Printf("Error sending message to the client:\n%s\n", err)
	// 		return
	// 	}
	// })

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
