package it.unibo.sap.application

import it.unibo.sap.application.exceptions.RideAlreadyEnded
import it.unibo.sap.application.exceptions.RideNotFound
import it.unibo.sap.domain.Ride
import it.unibo.sap.domain.model.RideModel
import it.unibo.sap.infrastructure.services.escooters.EScootersRepository
import it.unibo.sap.infrastructure.services.users.UsersRepository
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Ride service used to manage rides.
 */
interface RideService {
    val rideModel: RideModel
    var rideDashboardPort: RideDashboardPort?
    val usersRepository: UsersRepository
    val escotersRepository: EScootersRepository

    /**
     * Sets the ride dashboard port.
     * @param rideDashboardPort the ride dashboard port
     * @return the ride service with the ride dashboard port set
     */
    fun setRideDashboardPort(rideDashboardPort: RideDashboardPort): RideService

    /**
     * Starts a new ride.
     * @param userId the id of the user starting the ride
     * @param escooterId the id of the escooter used for the ride
     * @return the new ride or an exception if the user or the escooter do not exist
     */
    fun startNewRide(userId: String, escooterId: String): Result<Ride>

    /**
     * Gets a ride by its id.
     * @param id the id of the ride
     * @return the ride or an exception if the ride is not found
     */
    fun getRide(id: String): Result<Ride>

    /**
     * Ends a ride by its id.
     * @param id the id of the ride
     * @return the ended ride or an exception if the ride has already ended
     */
    fun endRide(id: String): Result<Ride>
}

class RideServiceImpl private constructor(
    override val rideModel: RideModel,
    override var rideDashboardPort: RideDashboardPort?,
    override val usersRepository: UsersRepository,
    override val escotersRepository: EScootersRepository
) : RideService {
    companion object {
        fun new(rideModel: RideModel, userRepository: UsersRepository, escotersRepository: EScootersRepository) =
            RideServiceImpl(rideModel, null, userRepository, escotersRepository)
    }

    val logger: Logger = Logger.getLogger("[RideService]")

    override fun setRideDashboardPort(rideDashboardPort: RideDashboardPort): RideService {
        this.rideDashboardPort = rideDashboardPort
        return this
    }

    override fun startNewRide(userId: String, escooterId: String): Result<Ride> {
        logger.log(Level.INFO, "Registering new ride")
        if (!this.usersRepository.userExists(userId) || !this.escotersRepository.escooterExists(escooterId)) {
            return Result.failure(IllegalArgumentException("Cannot start ride"))
        }
        val ride = Ride(userId, escooterId)
        return rideModel.addNewRide(ride)
            .onSuccess { rideDashboardPort?.notifyOngoingRidesChanged(rideModel.getOngoingRides()) }
    }

    override fun getRide(id: String): Result<Ride> {
        logger.log(Level.INFO, "Getting ride with id $id")
        return rideModel.getRide(id)
    }

    override fun endRide(id: String): Result<Ride> {
        logger.log(Level.INFO, "Ending ride with id $id")
        return getRide(id).fold(onFailure = { Result.failure(RideNotFound()) }, onSuccess = {
            logger.log(Level.INFO, "got the ride")
            if (it.isOngoing) {
                rideModel.endRide(it.end()).also {
                    rideDashboardPort?.notifyOngoingRidesChanged(rideModel.getOngoingRides())
                }
            } else {
                Result.failure(RideAlreadyEnded())
            }
        })
    }
}

/**
 * Creates a new ride service.
 * @param rideModel the ride model
 * @param userRepository the users repository
 * @param escotersRepository the escooters repository
 */
fun RideService(
    rideModel: RideModel,
    userRepository: UsersRepository = UsersRepository(),
    escotersRepository: EScootersRepository = EScootersRepository()
) = RideServiceImpl.new(rideModel, userRepository, escotersRepository)
