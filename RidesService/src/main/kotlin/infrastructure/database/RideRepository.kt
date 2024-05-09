package it.unibo.sap.infrastructure.database

import it.unibo.sap.domain.Ride

/**
 * Repository used to manage rides.
 */
interface RideRepository {
    /**
     * Saves a ride.
     * @param ride the ride to save
     * @return the saved ride or an exception if the ride could not be saved
     */
    fun saveRide(ride: Ride): Result<Ride>

    /**
     * Gets a ride by its id.
     * @param rideId the id of the ride
     * @return the ride or an exception if the ride is not found
     */
    fun getRide(rideId: String): Result<Ride>

    /**
     * Gets the id for the next ride to be created.
     * @return the next ride id
     */
    fun getNextRideId(): String

    /**
     * Gets all the rides.
     * @return the rides
     */
    fun getAllRides(): Sequence<Ride>

    /**
     * Updates a ride.
     * @param ride the ride to update
     * @return the updated ride or an exception if the ride could not be updated
     */
    fun updateRide(ride: Ride): Result<Ride>
}
