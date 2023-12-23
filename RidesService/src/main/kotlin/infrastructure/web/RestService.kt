package it.unibo.sap.infrastructure.web

import infrastructure.web.RestServiceVerticle
import it.unibo.sap.infrastructure.web.handlers.RideHandler
import io.vertx.core.Vertx

class RestService(
    val port: Int,
    val rideHandler: RideHandler
) {
    fun init() {
        Vertx.vertx().apply { deployVerticle(RestServiceVerticle(port, rideHandler)) }
    }

}