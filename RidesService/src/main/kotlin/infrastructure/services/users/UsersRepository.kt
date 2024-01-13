package it.unibo.sap.infrastructure.services.users

import org.json.JSONException

interface UsersRepository {
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

fun UsersRepository(): UsersRepository = UsersRepositoryImpl()