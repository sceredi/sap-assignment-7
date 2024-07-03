package it.unibo.sap.infrastructure.web.handlers

import it.unibo.sap.application.RideDashboardPort
import it.unibo.sap.domain.Ride
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.http.ServerWebSocket
import io.vertx.core.impl.logging.LoggerFactory
import io.vertx.core.json.JsonObject

class OngoingRidesWebSocketHandler : Handler<ServerWebSocket>, RideDashboardPort {
    private val logger = LoggerFactory.getLogger(OngoingRidesWebSocketHandler::class.java)
    override fun handle(event: ServerWebSocket?) {
        event?.let { webSocket ->
            logger.info(webSocket.path())
            if (webSocket.path() == "/api/rides/monitoring") {
                webSocket.accept()
                logger.info("New ride monitoring observer registered")
                Vertx.vertx().eventBus().consumer("ride-events") { msg ->
                    msg.body()?.let { body ->
                        JsonObject(body.toString()).let { jsonObject ->
                            logger.info("Changes in rides: ${jsonObject.encodePrettily()}")
                            event.writeTextMessage(jsonObject.encodePrettily())
                        }
                    }
                }
            } else {
                logger.info("Monitoring observer rejected")
            }
        }
    }

    override fun notifyOngoingRidesChanged(ongoingRides: Sequence<Ride>) {
        logger.info("notify num rides changed")

    }
}