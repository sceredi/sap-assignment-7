package infrastructure.database

import domain.User
import application.exceptions.UserNotFound
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Projections
import com.mongodb.kotlin.client.coroutine.MongoCollection
import domain.MongoUser
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId

interface UserRepository {
    fun saveUser(user: User): Result<User>
    fun getUser(userId: String): Result<User>
}


interface UserMongoRepository : UserRepository {
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

fun UserMongoRepository(collection: MongoCollection<MongoUser>) = UserMongoRepositoryImpl(collection)
