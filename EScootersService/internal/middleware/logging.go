package middleware

import (
	"fmt"
	"net/http"
	"time"

	log "github.com/sirupsen/logrus"
)

type wrappedWriter struct {
	http.ResponseWriter
	stratusCode int
}

// Overrides the WriteHeader function allowing the logging of the statusCode.
func (w *wrappedWriter) WriteHeader(statusCode int) {
	w.ResponseWriter.WriteHeader(statusCode)
	w.stratusCode = statusCode
}

// A logging middleware, prints to stdout all the information about the request
// and the relative response. next is the handler that will be wrapped into the
// logging middleware.
func Logging(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		wrapped := &wrappedWriter{
			ResponseWriter: w,
			stratusCode:    http.StatusOK,
		}

		next.ServeHTTP(wrapped, r)

		log.Info(fmt.Sprintf("%d %s %s %s\n", wrapped.stratusCode, r.Method, r.URL.Path, time.Since(start)))
	})
}
