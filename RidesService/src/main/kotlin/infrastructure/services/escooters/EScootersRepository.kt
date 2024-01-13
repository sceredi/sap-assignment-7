package it.unibo.sap.infrastructure.services.escooters

import org.json.JSONException

interface EScootersRepository {
    fun escooterExists(id: String): Boolean
}

class EScootersRepositoryImpl : EScootersRepository {
    override fun escooterExists(id: String): Boolean {
        val res: Boolean
        try {
            res = khttp.get("http://api-gateway:8080/api/escooters/$id").jsonObject.get("result") == "ok"
        } catch (e: JSONException) {
            return false
        }
        return res
    }
}

fun EScootersRepository(): EScootersRepository = EScootersRepositoryImpl()
