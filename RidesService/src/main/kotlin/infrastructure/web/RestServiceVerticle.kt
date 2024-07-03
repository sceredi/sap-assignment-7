package infrastructure.web

import io.vertx.core.AbstractVerticle
import io.vertx.core.http.HttpMethod
import io.vertx.core.impl.logging.LoggerFactory
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.StaticHandler
import it.unibo.sap.application.RideDashboardPort
import it.unibo.sap.domain.Ride
import it.unibo.sap.infrastructure.web.handlers.RideHandler


/**
 * Verticle that manages the REST service.
 */
interface RestServiceVerticle {
    val port: Int
    val rideHandler: RideHandler

    /**
     * Starts the service.
     */
    fun start()
}

class RestServiceVerticleImpl(
    override val port: Int,
    override val rideHandler: RideHandler,
) : RestServiceVerticle, AbstractVerticle(), RideDashboardPort {
    init {
        rideHandler.rideService.setRideDashboardPort(this)
    }

    private val logger = LoggerFactory.getLogger(RestServiceVerticle::class.java)

    @Volatile
    private var counter = 0


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


            route(HttpMethod.POST, "/rides").handler(rideHandler::startNewRide)
            route(HttpMethod.GET, "/rides/:rideId").handler(rideHandler::getRide)
            route(HttpMethod.POST, "/rides/:rideId/end").handler(rideHandler::endRide)
            route(HttpMethod.GET, "/health").handler { context: RoutingContext ->
                context.sendReply(JsonObject().put("status", "UP"))
            }
            route(HttpMethod.POST, "/kill").handler(rideHandler::kill)
            route(HttpMethod.GET, "/metrics").handler { context -> rideHandler.metrics(context, counter) }

        }

        server.apply {
            webSocketHandler { event ->
                event?.let { webSocket ->
                    logger.info(webSocket.path())
                    if (webSocket.path() == "/rides/monitoring") {
                        webSocket.accept()
                        logger.info("New ride monitoring observer registered")
                        vertx.eventBus().consumer("ride-events") { msg ->
                            msg.body()?.let { body ->
                                JsonObject(body.toString()).let { jsonObject ->
                                    logger.info("Changes in rides: ${jsonObject.encodePrettily()}")
                                    event.writeTextMessage(jsonObject.encodePrettily())
                                }
                            }
                        }
                        notifyOngoingRidesChanged(rideHandler.rideService.rideModel.getOngoingRides())
                    } else {
                        logger.info("Monitoring observer rejected")
                    }
                }
            }

            requestHandler(router)
            listen(port)
        }

        logger.info("Service ready, listening on port $port")
    }

    override fun notifyOngoingRidesChanged(ongoingRides: Sequence<Ride>) {
        logger.info("notify num rides changed")
        vertx.eventBus().apply {
            publish(
                "ride-events",
                JsonObject().put("event", "num-ongoing-rides-changed").put("nOngoingRides", ongoingRides.count())
            )
        }
    }

}

/**
 * Sends a reply to the client.
 * @param message the message to send
 */
fun RoutingContext.sendReply(message: JsonObject) {
    this.response().putHeader("content-type", "application/json").end(message.toString())
}

/**
 * Creates a new rest service verticle.
 * @param port the port on which the service will listen
 * @param rideHandler the ride handler used to manage rides
 * @return the new rest service verticle
 */
fun RestServiceVerticle(
    port: Int,
    rideHandler: RideHandler,
) = RestServiceVerticleImpl(port, rideHandler)
