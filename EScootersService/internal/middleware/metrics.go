package middleware

import (
	"log"
	"net/http"
)

type Metrics struct {
	Requests_total int
}

var metric = Metrics{
	Requests_total: 0,
}

func GetMetric() Metrics {
	return metric
}

func UpdateMetric(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		metric.Requests_total += 1
		log.Printf("Total requests: %d\n", GetMetric().Requests_total)
		next.ServeHTTP(w, r)
	})

}
