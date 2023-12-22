package domain.model

import domain.User
import infrastructure.database.UserRepository

interface UserModel {
    val databasePort: UserRepository

    fun addNewUser(user: User): Result<User>
    fun getUser(id: String): Result<User>
}

class UserModelImpl(override val databasePort: UserRepository) : UserModel {
    override fun addNewUser(user: User): Result<User> = databasePort.saveUser(user)

    override fun getUser(id: String): Result<User> = databasePort.getUser(id)
}

fun UserModel(databasePort: UserRepository) = UserModelImpl(databasePort)
