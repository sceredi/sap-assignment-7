package infrastructure.web

import infrastructure.web.handlers.UserHandler
import io.vertx.core.AbstractVerticle
import io.vertx.core.http.HttpMethod
import io.vertx.core.impl.logging.LoggerFactory
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.StaticHandler

/**
 * Represents the REST service
 */
interface RestServiceVerticle {
    val port: Int
    val userHandler: UserHandler
    fun start()
}

class RestServiceVerticleImpl(
    override val port: Int,
    override val userHandler: UserHandler,
) : RestServiceVerticle, AbstractVerticle() {

    private val logger = LoggerFactory.getLogger(RestServiceVerticle::class.java)

    @Volatile
    private var counter = 0


    /**
     * Initializes the REST service
     */
    override fun start() {
        logger.info("Service initializing...")
        val server = vertx.createHttpServer()
        val router = Router.router(vertx)

        router.apply {
            route("/static/*").handler(StaticHandler.create().setCachingEnabled(false))
            route().handler(BodyHandler.create())
            route().handler {
                counter += 1
                it.next()
            }

            route(HttpMethod.POST, "/users").handler(userHandler::registerNewUser)
            route(HttpMethod.GET, "/users/:userId").handler(userHandler::getUser)
            route(HttpMethod.GET, "/health").handler { context: RoutingContext ->
                context.sendReply(JsonObject().put("status", "UP"))
            }
            route(HttpMethod.POST, "/kill").handler(userHandler::kill)
            route(HttpMethod.GET, "/metrics").handler { context -> userHandler.metrics(context, counter) }
        }

        server.apply {
            requestHandler(router)
            listen(port)
        }

        logger.info("Service ready again, listening on port $port")
    }
}

/**
 * Utility extension function that sends a reply to the client
 */
fun RoutingContext.sendReply(message: JsonObject) {
    this.response().putHeader("content-type", "application/json").end(message.toString())
}

fun RestServiceVerticle(
    port: Int,
    userHandler: UserHandler,
) = RestServiceVerticleImpl(port, userHandler)