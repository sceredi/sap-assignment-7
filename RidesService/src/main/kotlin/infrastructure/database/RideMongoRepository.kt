package it.unibo.sap.infrastructure.database

import it.unibo.sap.application.exceptions.RideNotFound
import com.mongodb.client.model.*
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoCollection
import it.unibo.sap.domain.MongoRide
import it.unibo.sap.domain.Ride
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId

/**
 * Ride repository used to manage rides in the mongodb database.
 */
interface RideMongoRepository : RideRepository {
    /**
     * The mongodb collection used to manage rides.
     */
    val collection: MongoCollection<MongoRide>
}

class RideMongoRepositoryImpl(override val collection: MongoCollection<MongoRide>) : RideMongoRepository {
    private val projectionFields = Projections.fields(
        Projections.include(
            Ride::id.name,
            Ride::escooterId.name,
            Ride::userId.name,
            Ride::startDate.name,
            Ride::endDate.name
        )
    )

    override fun saveRide(ride: Ride): Result<Ride> = runBlocking {
        runCatching {
            collection.insertOne(
                ride.toMongoRide()
            )
            ride
        }
    }

    override fun getRide(rideId: String): Result<Ride> = runBlocking {
        collection.find(eq(Ride::id.name, rideId)).projection(projectionFields).firstOrNull()?.let {
            Result.success(it)
        } ?: Result.failure(RideNotFound())
    }

    override fun getNextRideId(): String = runBlocking {
        collection.find()
            .sort(Sorts.descending("id"))
            .limit(1)
            .projection(projectionFields)
            .firstOrNull()?.let {
                it.id?.substringAfterLast('-')?.toIntOrNull()?.let {
                    "ride-${it + 1}"
                }
            } ?: "ride-1"
    }

    override fun getAllRides(): Sequence<Ride> = runBlocking {
        collection.find().toList().asSequence()
    }

    override fun updateRide(ride: Ride): Result<Ride> = runBlocking {
        runCatching {
            val query = eq(Ride::id.name, ride.id)
            val updates = Updates.combine(
                Updates.set(Ride::id.name, ride.id),
                Updates.set(Ride::userId.name, ride.userId),
                Updates.set(Ride::escooterId.name, ride.escooterId),
                Updates.set(Ride::startDate.name, ride.endDate),
                Updates.set(Ride::endDate.name, ride.endDate)
            )
            val options = UpdateOptions().upsert(true)
            collection.updateOne(query, updates, options)
            ride
        }
    }
}

/**
 * Converts a ride to a mongo ride.
 * @return the mongo ride
 */
fun Ride.toMongoRide() = MongoRide(ObjectId(), id, userId, escooterId, startDate, endDate)

/**
 * Creates a new ride mongo repository.
 * @param collection the mongodb collection used to manage rides
 * @return the new ride mongo repository
 */
fun RideMongoRepository(collection: MongoCollection<MongoRide>) = RideMongoRepositoryImpl(collection)