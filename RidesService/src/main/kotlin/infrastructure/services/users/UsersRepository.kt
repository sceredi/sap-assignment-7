package it.unibo.sap.infrastructure.services.users

import org.json.JSONException

/**
 * Repository used to manage users.
 */
interface UsersRepository {
    /**
     * Checks if a user exists.
     * @param id the id of the user
     * @return true if the user exists, false otherwise
     */
    fun userExists(id: String): Boolean
}

class UsersRepositoryImpl : UsersRepository {
    override fun userExists(id: String): Boolean {
        val res: Boolean
        try {
            res = khttp.get("http://api-gateway:8080/api/users/$id").jsonObject.get("result") == "ok"
        } catch (e: JSONException) {
            return false
        }
        return res
    }
}

/**
 * Creates a new users repository.
 * @return the new users repository
 */
fun UsersRepository(): UsersRepository = UsersRepositoryImpl()