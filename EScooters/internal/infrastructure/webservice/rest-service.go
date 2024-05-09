package webservice

import (
	"net/http"

	"github.com/sceredi/sap-assignment-5/escooters-service/internal/middleware"
)

func Serve(addr string) {
	router := http.NewServeMux()
	loadHandlers(router)
	stack := middleware.CreateStack(
		middleware.Logging,
	)
	server := http.Server{
		Addr:    addr,
		Handler: stack(router),
	}
	server.ListenAndServe()
}
