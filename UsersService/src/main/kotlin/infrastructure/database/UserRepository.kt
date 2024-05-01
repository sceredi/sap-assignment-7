package infrastructure.database

import application.exceptions.UserNotFound
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Projections
import com.mongodb.kotlin.client.coroutine.MongoCollection
import domain.MongoUser
import domain.User
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId

/**
 * Represents the user repository
 */
interface UserRepository {
    /**
     * Saves a user
     * @param user User to save
     * @return Result<User>
     */
    fun saveUser(user: User): Result<User>

    /**
     * Gets a user by its id
     * @param userId User id
     * @return Result<User>
     */
    fun getUser(userId: String): Result<User>
}

/**
 * Represents the user repository in MongoDB
 */
interface UserMongoRepository : UserRepository {
    /**
     * The collection to interact with
     */
    val collection: MongoCollection<MongoUser>
}


class UserMongoRepositoryImpl(override val collection: MongoCollection<MongoUser>) : UserMongoRepository {
    private val projectionFields = Projections.fields(
        Projections.include(User::id.name, User::name.name, User::surname.name),
    )

    override fun saveUser(user: User): Result<User> = runBlocking {
        runCatching {
            collection.insertOne(MongoUser(ObjectId(), user.id, user.name, user.surname))
            user
        }
    }

    override fun getUser(userId: String): Result<User> = runBlocking {
        collection.find(eq(User::id.name, userId)).projection(projectionFields).firstOrNull()?.let {
            Result.success(it)
        } ?: Result.failure(UserNotFound())
    }
}

/**
 * Factory function to create a UserMongoRepository
 * @param collection The collection to interact with
 * @return UserMongoRepository
 */
fun UserMongoRepository(collection: MongoCollection<MongoUser>) = UserMongoRepositoryImpl(collection)
