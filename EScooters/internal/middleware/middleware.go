package middleware

import "net/http"

type Middleware func(http.Handler) http.Handler

// Instantiates a Middlware, xs is the sequence of Middlewares that will be called
func CreateStack(xs ...Middleware) Middleware {
	return func(next http.Handler) http.Handler {
		for _, x := range xs {
			next = x(next)
		}
		return next
	}
}
