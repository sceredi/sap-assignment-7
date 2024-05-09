package it.unibo.sap.domain.model

import it.unibo.sap.domain.Ride
import it.unibo.sap.infrastructure.database.RideRepository

/**
 * Ride model used to manage rides.
 */
interface RideModel {
    /**
     * The database port used to interact with the database.
     */
    val databasePort: RideRepository

    /**
     * Adds a new ride.
     * @param ride the ride to add
     * @return the new ride or an exception if the ride could not be added
     */
    fun addNewRide(ride: Ride): Result<Ride>

    /**
     * Gets a ride by its id.
     * @param id the id of the ride
     * @return the ride or an exception if the ride is not found
     */
    fun getRide(id: String): Result<Ride>

    /**
     * Ends a ride.
     * @param ride the ride to end
     * @return the ended ride or an exception if the ride has already ended
     */
    fun endRide(ride: Ride): Result<Ride>

    /**
     * Gets all the ongoing rides.
     * @return the ongoing rides
     */
    fun getOngoingRides(): Sequence<Ride>
}

class RideModelImpl(override val databasePort: RideRepository) : RideModel {
    override fun addNewRide(ride: Ride): Result<Ride> = databasePort.saveRide(ride.setId(databasePort.getNextRideId()))

    override fun getRide(id: String): Result<Ride> = databasePort.getRide(id)
    override fun endRide(ride: Ride): Result<Ride> = databasePort.updateRide(ride)
    override fun getOngoingRides(): Sequence<Ride> = databasePort.getAllRides().filter { it.isOngoing }
}

/**
 * Creates a new ride model.
 * @param databasePort the database port used to interact with the database
 * @return the new ride model
 */
fun RideModel(databasePort: RideRepository) = RideModelImpl(databasePort)
