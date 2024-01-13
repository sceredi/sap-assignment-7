package it.unibo.sap.infrastructure.services.users

interface UsersRepository {
    fun userExists(id: String): Boolean
}

class UsersRepositoryImpl : UsersRepository {
    override fun userExists(id: String): Boolean {
        return khttp.get("http://api-gateway:8080/api/users/$id").jsonObject.get("result") == "ok"
    }
}

fun UsersRepository(): UsersRepository = UsersRepositoryImpl()