package application

import com.mongodb.MongoException
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import it.unibo.sap.application.RideService
import it.unibo.sap.application.exceptions.RideNotFound
import it.unibo.sap.domain.Ride
import it.unibo.sap.domain.model.RideModel
import it.unibo.sap.infrastructure.database.RideRepository
import it.unibo.sap.infrastructure.services.escooters.EScootersRepository
import it.unibo.sap.infrastructure.services.users.UsersRepository
import java.time.LocalDateTime

class RideServiceTest : ShouldSpec({
    val goodNewRide = Ride(userId = "1", escooterId = "1")
    val badNewRide = Ride(userId = "2", escooterId = "2")

    val existingRide = Ride(id = "1", userId = "1", escooterId = "1", startDate = LocalDateTime.now(), endDate = null)
    val nonExisingRide =
        Ride(id = "2", userId = "2", escooterId = "2", startDate = LocalDateTime.now(), endDate = LocalDateTime.now())
    val repositoryMock = mockk<RideRepository> {
        every { saveRide(any()) } returns Result.success(goodNewRide)

        every { getRide("1") } returns Result.success(existingRide)
        every { getRide("2") } returns Result.failure(RideNotFound())

        every { getNextRideId() } returns "3"
        every { getAllRides() } returns sequenceOf(existingRide)

        every { updateRide(any()) } returns Result.success(existingRide)
    }
    val badRepositoryMock = mockk<RideRepository> {
        every { saveRide(any()) } returns Result.failure(MongoException(1, "error"))
        every { getNextRideId() } returns "3"
    }

    val usersRepositoryMock = mockk<UsersRepository> {
        every { userExists("1") } returns true
        every { userExists("2") } returns false
    }

    val escotersRepositoryMock = mockk<EScootersRepository> {
        every { escooterExists("1") } returns true
        every { escooterExists("2") } returns false
    }

    context("Having a RideService") {
        val rideService = RideService(RideModel(repositoryMock), usersRepositoryMock, escotersRepositoryMock)

        should("start a new ride") {
            rideService.startNewRide("1", "1").getOrNull() shouldBe goodNewRide
        }

        should("get a ride") {
            rideService.getRide("1").getOrNull() shouldBe existingRide
        }

        should("fail getting a non existing ride") {
            rideService.getRide("2").exceptionOrNull() shouldBe RideNotFound()
        }

        should("fail starting a ride with a non existing user") {
            rideService.startNewRide("2", "1").exceptionOrNull() shouldBe IllegalArgumentException("Cannot start ride")
        }

        should("fail starting a ride with a non existing escooter") {
            rideService.startNewRide("1", "2").exceptionOrNull() shouldBe IllegalArgumentException("Cannot start ride")
        }

        should("fail starting a ride with a non existing user and escooter") {
            rideService.startNewRide("2", "2").exceptionOrNull() shouldBe IllegalArgumentException("Cannot start ride")
        }

        should("end a ride") {
            rideService.endRide("1").getOrNull() shouldBe existingRide
        }

        should("fail ending a non existing ride") {
            rideService.endRide("2").exceptionOrNull() shouldBe RideNotFound()
        }
    }

    context("Having a RideService with a bad repository") {
        val rideService = RideService(RideModel(badRepositoryMock), usersRepositoryMock, escotersRepositoryMock)

        should("fail starting a new ride") {
            rideService.startNewRide("1", "1").exceptionOrNull() shouldBe MongoException(1, "error")
        }
    }
})

