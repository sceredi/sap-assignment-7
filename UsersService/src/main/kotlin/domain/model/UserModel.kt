package domain.model

import domain.User
import infrastructure.database.UserRepository

/**
 * Represents the user model
 */
interface UserModel {
    /**
     * The database
     */
    val databasePort: UserRepository

    /**
     * Adds a new user
     */
    fun addNewUser(user: User): Result<User>
    /**
     * Gets a user by its id
     */
    fun getUser(id: String): Result<User>
}

class UserModelImpl(override val databasePort: UserRepository) : UserModel {
    override fun addNewUser(user: User): Result<User> = databasePort.saveUser(user)

    override fun getUser(id: String): Result<User> = databasePort.getUser(id)
}

/**
 * Factory function to create a UserModel
 * @param databasePort User repository to interact with the database
 * @return UserModel
 */
fun UserModel(databasePort: UserRepository) = UserModelImpl(databasePort)
