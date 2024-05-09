package it.unibo.sap.domain

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalDateTime

/**
 * Represents a ride.
 */
interface Ride {
    val isOngoing: Boolean
    val id: String?
    val userId: String
    val escooterId: String
    val startDate: LocalDateTime
    val endDate: LocalDateTime?

    /**
     * Sets the id of the ride.
     * @param id the id of the ride
     * @return the ride
     */
    fun setId(id: String): Ride

    /**
     * Ends the ride.
     * @return the ride
     */
    fun end(): Ride
}

open class RideImpl(
    override val id: String?,
    override val userId: String,
    override val escooterId: String,
    override val startDate: LocalDateTime,
    override val endDate: LocalDateTime?,
) : Ride {
    override val isOngoing: Boolean
        get() = endDate == null

    override fun setId(id: String) = Ride(id, userId, escooterId, startDate, endDate)
    override fun end(): Ride = Ride(id!!, userId, escooterId, startDate, LocalDateTime.now())

    companion object {
        fun new(userId: String, escooterId: String) = new(null, userId, escooterId)
        private fun new(id: String?, userId: String, escooterId: String) =
            new(id, userId, escooterId, LocalDateTime.now(), null)

        fun new(id: String?, userId: String, escooterId: String, startDate: LocalDateTime, endDate: LocalDateTime?) =
            RideImpl(id, userId, escooterId, startDate, endDate)
    }
}

data class MongoRide(
    @BsonId val objectId: ObjectId?,
    override val id: String?, override val userId: String, override val escooterId: String,
    override val startDate: LocalDateTime, override val endDate: LocalDateTime?
) : RideImpl(id, userId, escooterId, startDate, endDate)

/**
 * Creates a new ride.
 * @param userId the id of the user starting the ride
 * @param escooterId the id of the escooter used for the ride
 * @return the ride
 */
fun Ride(userId: String, escooterId: String) = RideImpl.new(userId, escooterId)

/**
 * Creates a new ride.
 * @param id the id of the ride
 * @param userId the id of the user starting the ride
 * @param escooterId the id of the escooter used for the ride
 * @param startDate the start date of the ride
 * @param endDate the end date of the ride
 * @return the ride
 */
fun Ride(id: String, userId: String, escooterId: String, startDate: LocalDateTime, endDate: LocalDateTime?) =
    RideImpl.new(id, userId, escooterId, startDate, endDate)


