package handler

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"

	"github.com/sceredi/sap-assignment-5/escooters-service/internal/core/domain"
	"github.com/sceredi/sap-assignment-5/escooters-service/internal/core/services"
)

type EScootersHandler struct {
	svc *services.EScootersService
}

func NewEScootersHandler(escootersService *services.EScootersService) *EScootersHandler {
	return &EScootersHandler{
		svc: escootersService,
	}
}

func (h *EScootersHandler) RegisterEScooter(w http.ResponseWriter, r *http.Request) {
	type RequestBody struct {
		EscooterId string `json:"escooterId"`
	}
	type ResultBody struct {
		Result   string          `json:"result"`
		EScooter domain.EScooter `json:"escooter"`
	}
	dec := json.NewDecoder(r.Body)
	dec.DisallowUnknownFields()
	var rb RequestBody
	err := dec.Decode(&rb)
	if err != nil {
		http.Error(w, fmt.Sprintf("escootedId required\n%s\n", err), http.StatusBadRequest)
		return
	}
	log.Printf("New register request, id: %s\n", rb.EscooterId)
	escooter, err := h.svc.RegisterEScooter(rb.EscooterId)
	if err != nil {
		http.Error(w, fmt.Sprintf("Error occurred registering the escooter\n%s\n", err), http.StatusPreconditionFailed)
		return
	}
	response := ResultBody{
		Result:   "Ok",
		EScooter: *escooter,
	}
	renderJSON(w, response)
}

// renderJSON renders 'v' as JSON and writes it as a response into w.
func renderJSON(w http.ResponseWriter, v interface{}) {
	js, err := json.Marshal(v)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	w.Header().Set("Content-Type", "application/json")
	w.Write(js)
}
