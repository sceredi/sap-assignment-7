package domain.model

import com.mongodb.client.result.InsertOneResult
import com.mongodb.kotlin.client.coroutine.MongoCollection
import domain.MongoUser
import infrastructure.database.UserMongoRepository
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.coEvery
import io.mockk.mockk
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class UserModelTest : ShouldSpec({
    context("Having a user model") {
        val collectionRepository = mockk<MongoCollection<MongoUser>>()
        val workingUser = MongoUser(ObjectId(), "1", "John", "Doe")
        val errorUser = MongoUser(ObjectId(), "2", "NotJohn", "NotDoe")
        coEvery { collectionRepository.insertOne(workingUser) } returns InsertOneResult.acknowledged(null)
        coEvery { collectionRepository.insertOne(errorUser) } throws Exception("Error")

        val userModel = UserModel(UserMongoRepository(collectionRepository))

        should("add a new user") {
            userModel.addNewUser(workingUser).onSuccess {
                assertEquals("1", it.id)
                assertEquals("John", it.name)
                assertEquals("Doe", it.surname)
            }
        }

        should("handle errors correctly") {
            assertTrue(userModel.addNewUser(errorUser).isFailure)
        }

    }
})