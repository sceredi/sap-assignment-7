package infrastructure.web

import infrastructure.web.handlers.UserHandler
import io.vertx.core.Vertx

class RestService(
    val port: Int,
    val userHandler: UserHandler,
) {
    fun init() {
        Vertx.vertx().apply { deployVerticle(RestServiceVerticle(port, userHandler)) }
    }

}