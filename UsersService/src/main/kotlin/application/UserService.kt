package application

import application.exceptions.UserAlreadyExists
import domain.User
import domain.model.UserModel
import java.util.logging.Level
import java.util.logging.Logger

interface UserService {
    val userModel: UserModel
    fun registerNewUser(id: String, name: String, surname: String): Result<User>
    fun getUser(id: String): Result<User>
}

class UserServiceImpl(override val userModel: UserModel) : UserService {
    val logger: Logger = Logger.getLogger("[UserService]")

    override fun registerNewUser(id: String, name: String, surname: String): Result<User> {
        logger.log(Level.INFO, "Registering new user")
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

fun UserService(userModel: UserModel) = UserServiceImpl(userModel)
