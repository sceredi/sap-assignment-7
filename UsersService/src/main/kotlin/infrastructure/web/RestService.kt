package infrastructure.web

import infrastructure.web.handlers.UserHandler
import io.vertx.core.Vertx

/**
 * Represents the REST service
 */
class RestService(
    private val port: Int,
    private val userHandler: UserHandler,
) {
    /**
     * Initializes the REST service
     */
    fun init() {
        Vertx.vertx().apply { deployVerticle(RestServiceVerticle(port, userHandler)) }
    }
}