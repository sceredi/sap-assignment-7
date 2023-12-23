package it.unibo.sap.infrastructure.web.handlers

import it.unibo.sap.application.RideDashboardPort
import it.unibo.sap.domain.Ride
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.http.ServerWebSocket
import io.vertx.core.json.JsonObject
import java.util.logging.Level
import java.util.logging.Logger

class OngoingRidesWebSocketHandler : Handler<ServerWebSocket>, RideDashboardPort {
    private val logger = Logger.getLogger("[WebSocketHandler]")
    override fun handle(event: ServerWebSocket?) {
        event?.let { webSocket ->
            logger.log(Level.INFO, webSocket.path())
            if (webSocket.path() == "/api/rides/monitoring") {
                webSocket.accept()
                logger.log(Level.INFO, "New ride monitoring observer registered")
                Vertx.vertx().eventBus().consumer("ride-events") { msg ->
                    msg.body()?.let { body ->
                        JsonObject(body.toString()).let { jsonObject ->
                            logger.log(Level.INFO, "Changes in rides: ${jsonObject.encodePrettily()}")
                            event.writeTextMessage(jsonObject.encodePrettily())
                        }
                    }
                }
            } else {
                logger.log(Level.INFO, "Monitoring observer rejected")
            }
        }
    }

    override fun notifyOngoingRidesChanged(ongoingRides: Sequence<Ride>) {
        logger.log(Level.INFO, "notify num rides changed")

    }
}