package it.unibo.sap.domain.model

import it.unibo.sap.domain.Ride
import it.unibo.sap.infrastructure.database.RideRepository

interface RideModel {
    val databasePort: RideRepository

    fun addNewRide(ride: Ride): Result<Ride>
    fun getRide(id: String): Result<Ride>
    fun endRide(ride: Ride): Result<Ride>
    fun getOngoingRides(): Sequence<Ride>
}

class RideModelImpl(override val databasePort: RideRepository) : RideModel {
    override fun addNewRide(ride: Ride): Result<Ride> = databasePort.saveRide(ride.setId(databasePort.getNextRideId()))

    override fun getRide(id: String): Result<Ride> = databasePort.getRide(id)
    override fun endRide(ride: Ride): Result<Ride> = databasePort.updateRide(ride)
    override fun getOngoingRides(): Sequence<Ride> = databasePort.getAllRides().filter { it.isOngoing }
}

fun RideModel(databasePort: RideRepository) = RideModelImpl(databasePort)
