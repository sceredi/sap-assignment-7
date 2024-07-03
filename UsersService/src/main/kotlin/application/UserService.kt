package application

import application.exceptions.UserAlreadyExists
import domain.User
import domain.model.UserModel
import io.vertx.core.impl.logging.LoggerFactory

/**
 * Service to handle user operations
 */
interface UserService {
    /**
     * User model to interact with the database
     */
    val userModel: UserModel
    /**
     * Register a new user
     * @param id User id
     * @param name User name
     * @param surname User surname
     * @return Result<User> with the user registered
     */
    fun registerNewUser(id: String, name: String, surname: String): Result<User>
    /**
     * Get a user by its id
     * @param id User id
     * @return Result<User> with the user found
     */
    fun getUser(id: String): Result<User>
}

class UserServiceImpl(override val userModel: UserModel) : UserService {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    override fun registerNewUser(id: String, name: String, surname: String): Result<User> {
        logger.info("Registering new user")
        val user = User(id, name, surname)
        return getUser(id).fold(
            onFailure = {
                userModel.addNewUser(user).onSuccess {
                    Result.success(it)
                }.onFailure {
                    Result.failure<User>(it)
                }
            },
            onSuccess = {
                Result.failure(UserAlreadyExists())
            }
        )
    }

    override fun getUser(id: String): Result<User> = userModel.getUser(id)
}

/**
 * Factory function to create a UserService
 * @param userModel User model to interact with the database
 * @return UserService
 */
fun UserService(userModel: UserModel) = UserServiceImpl(userModel)
