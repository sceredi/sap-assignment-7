package infrastructure.web

import io.vertx.core.AbstractVerticle
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.StaticHandler
import it.unibo.sap.application.RideDashboardPort
import it.unibo.sap.domain.Ride
import it.unibo.sap.infrastructure.web.handlers.RideHandler
import java.util.logging.Level
import java.util.logging.Logger


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

    private val logger = Logger.getLogger("[RestService]")


    override fun start() {
        logger.log(Level.INFO, "Service initializing...")
        val server = vertx.createHttpServer()
        val router = Router.router(vertx)

        router.apply {
            route("/static/*").handler(StaticHandler.create().setCachingEnabled(false))
            route().handler(BodyHandler.create())

            route(HttpMethod.POST, "/rides").handler(rideHandler::startNewRide)
            route(HttpMethod.GET, "/rides/:rideId").handler(rideHandler::getRide)
            route(HttpMethod.POST, "/rides/:rideId/end").handler(rideHandler::endRide)
        }

        server.apply {
            webSocketHandler { event ->
                event?.let { webSocket ->
                    logger.log(Level.INFO, webSocket.path())
                    if (webSocket.path() == "/api/rides/monitoring") {
                        webSocket.accept()
                        logger.log(Level.INFO, "New ride monitoring observer registered")
                        vertx.eventBus().consumer("ride-events") { msg ->
                            msg.body()?.let { body ->
                                JsonObject(body.toString()).let { jsonObject ->
                                    logger.log(Level.INFO, "Changes in rides: ${jsonObject.encodePrettily()}")
                                    event.writeTextMessage(jsonObject.encodePrettily())
                                }
                            }
                        }
                        notifyOngoingRidesChanged(rideHandler.rideService.rideModel.getOngoingRides())
                    } else {
                        logger.log(Level.INFO, "Monitoring observer rejected")
                    }
                }
            }

            requestHandler(router)
            listen(port)
        }

        logger.log(Level.INFO, "Service ready, listening on port $port")
    }

    private fun createWebPageLink(name: String) = "http://localhost:$port/static/${name}.html"

    override fun notifyOngoingRidesChanged(ongoingRides: Sequence<Ride>) {
        logger?.log(Level.INFO, "notify num rides changed")
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