package it.unibo.sap.domain

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalDateTime

interface Ride {
    val isOngoing: Boolean
    val id: String?
    val userId: String
    val escooterId: String
    val startDate: LocalDateTime
    val endDate: LocalDateTime?

    fun setId(id: String): Ride
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

fun Ride(userId: String, escooterId: String) = RideImpl.new(userId, escooterId)
fun Ride(id: String, userId: String, escooterId: String, startDate: LocalDateTime, endDate: LocalDateTime?) =
    RideImpl.new(id, userId, escooterId, startDate, endDate)


