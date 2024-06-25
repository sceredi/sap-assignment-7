package handler

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"os"

	"github.com/sceredi/sap-assignment-5/escooters-service/internal/core/domain"
	"github.com/sceredi/sap-assignment-5/escooters-service/internal/core/services"
	"github.com/sceredi/sap-assignment-5/escooters-service/internal/middleware"
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

// EScootersHandler is the handler for the escooter routes
type EScootersHandler struct {
	svc *services.EScootersService
}

func NewEScootersHandler(escootersService *services.EScootersService) *EScootersHandler {
	return &EScootersHandler{
		svc: escootersService,
	}
}

// RegisterEScooter is the handler for the escooter registation request
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
				Error: domain.ErrorNoGivenId,
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
				Error: err.Error(),
			},
		}
		renderError(w, result)
		return
	}
	response := okResultMsg{
		Result:   "ok",
		EScooter: *escooter,
	}
	renderJSON(w, response)
}

// GetEScooter is the handler for a get escooter request
func (h *EScootersHandler) GetEScooter(w http.ResponseWriter, r *http.Request) {
	id := r.PathValue("id")
	if id == "" {
		result := errorMsg{
			Code: http.StatusNotFound,
			Msg: errorResultMsg{
				Error: domain.ErrorNoGivenId,
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
				Error: err.Error(),
			},
		}
		renderError(w, result)
		return
	}
	response := okResultMsg{
		Result:   "ok",
		EScooter: *escooter,
	}
	renderJSON(w, response)
}

func (h *EScootersHandler) Kill(w http.ResponseWriter, r *http.Request) {
	os.Exit(1)
}

func (h *EScootersHandler) Metrics(w http.ResponseWriter, r *http.Request) {
	metric := middleware.GetMetric()
	help := fmt.Sprint("# HELP requests_total The total number of received requests")
	msg_type := fmt.Sprint("# TYPE requests_total counter")
	counter := fmt.Sprintf("requests_total %d", metric.Requests_total)
	msg := fmt.Sprintf("%s\n%s\n%s\n", help, msg_type, counter)
	w.Header().Set("Content-Type", "text/plain")
	_, err := w.Write([]byte(msg))
	if err != nil {
		http.Error(w, "Unable to write the response", http.StatusInternalServerError)
	}
}

// NotFound renders a 404 message for any route that isn't currently available
func (h *EScootersHandler) NotFound(w http.ResponseWriter, r *http.Request) {
	result := errorMsg{
		Code: 404,
		Msg: errorResultMsg{
			Error: domain.ErrorEndpointNotFound,
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

// renderError renders an error as JSON
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
