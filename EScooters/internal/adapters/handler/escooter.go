package handler

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"

	"github.com/sceredi/sap-assignment-5/escooters-service/internal/core/domain"
	"github.com/sceredi/sap-assignment-5/escooters-service/internal/core/services"
)

type okResultMsg struct {
	Result   string          `json:"result"`
	EScooter domain.EScooter `json:"escooter"`
}

type errorMsg struct {
	Code int
	Msg  errorResultMsg
}

type errorResultMsg struct {
	Error string `json:"error"`
}

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
	dec := json.NewDecoder(r.Body)
	dec.DisallowUnknownFields()
	var rb RequestBody
	err := dec.Decode(&rb)
	if err != nil {
		result := errorMsg{
			Code: http.StatusBadRequest,
			Msg: errorResultMsg{
				Error: fmt.Sprintf("escootedId required\n%s\n", err),
			},
		}
		renderError(w, result)
		return
	}
	log.Printf("New register request, id: %s\n", rb.EscooterId)
	escooter, err := h.svc.RegisterEScooter(rb.EscooterId)
	if err != nil {
		result := errorMsg{
			Code: http.StatusPreconditionFailed,
			Msg: errorResultMsg{
				Error: fmt.Sprintf("Error occurred registering the escooter\n%s\n", err),
			},
		}
		renderError(w, result)
		return
	}
	response := okResultMsg{
		Result:   "Ok",
		EScooter: *escooter,
	}
	renderJSON(w, response)
}

func (h *EScootersHandler) GetEScooter(w http.ResponseWriter, r *http.Request) {
	id := r.PathValue("id")
	if id == "" {
		result := errorMsg{
			Code: http.StatusBadRequest,
			Msg: errorResultMsg{
				Error: "Error: id path value is required",
			},
		}
		renderError(w, result)
		return
	}
	escooter, err := h.svc.GetEScooter(id)
	if err != nil {
		result := errorMsg{
			Code: http.StatusInternalServerError,
			Msg: errorResultMsg{
				Error: fmt.Sprintf("Error: %s", err),
			},
		}
		renderError(w, result)
		return
	}
	response := okResultMsg{
		Result:   "Ok",
		EScooter: *escooter,
	}
	renderJSON(w, response)
}

func (h *EScootersHandler) NotFound(w http.ResponseWriter, r *http.Request) {
	result := errorMsg{
		Code: 404,
		Msg: errorResultMsg{
			Error: "Error: endpoint not found",
		},
	}
	renderError(w, result)
}

// renderJSON renders 'v' as JSON and writes it as a response into w.
func renderJSON(w http.ResponseWriter, v interface{}) {
	js, err := json.Marshal(v)
	if err != nil {
		result := errorMsg{
			Code: http.StatusInternalServerError,
			Msg: errorResultMsg{
				Error: err.Error(),
			},
		}
		renderError(w, result)
		return
	}
	w.Header().Set("Content-Type", "application/json")
	w.Write(js)
}

func renderError(w http.ResponseWriter, msg errorMsg) {
	_, err := json.Marshal(msg.Msg)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(msg.Code)
	json.NewEncoder(w).Encode(msg.Msg)
}
