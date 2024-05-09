package it.unibo.sap.infrastructure.services.escooters

import org.json.JSONException

/**
 * Repository used to manage escooters.
 */
interface EScootersRepository {
    /**
     * Checks if an escooter exists.
     * @param id the id of the escooter
     * @return true if the escooter exists, false otherwise
     */
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

/**
 * Creates a new escooters repository.
 * @return the new escooters repository
 */
fun EScootersRepository(): EScootersRepository = EScootersRepositoryImpl()
