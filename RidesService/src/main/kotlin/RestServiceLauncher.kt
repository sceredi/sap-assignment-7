package it.unibo.sap

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import it.unibo.sap.application.RideService
import it.unibo.sap.domain.MongoRide
import it.unibo.sap.domain.model.RideModel
import it.unibo.sap.infrastructure.database.RideMongoRepository
import it.unibo.sap.infrastructure.web.RestService
import it.unibo.sap.infrastructure.web.handlers.RideHandler
import org.bson.UuidRepresentation
import org.bson.codecs.UuidCodec
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.pojo.PojoCodecProvider

class RestServiceLauncher {


    fun launch() {
        val port = 8080
        val connectionString = ConnectionString(System.getenv("MONGODB_URI") ?: "mongodb://rides-db:27017")
        val settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .codecRegistry(
                CodecRegistries.fromRegistries(
                    CodecRegistries.fromCodecs(UuidCodec(UuidRepresentation.STANDARD)),
                    MongoClientSettings.getDefaultCodecRegistry(),
                    CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
                )
            )
            .build()

        val client = MongoClient.create(settings)
        val db = client.getDatabase("rides_db")
        val rides = db.getCollection<MongoRide>("rides")

        val rideHandler = RideHandler(RideService(RideModel(RideMongoRepository(rides))))

        RestService(port, rideHandler).init()

    }
}