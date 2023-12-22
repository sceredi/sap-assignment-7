package domain

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

interface User {
    val id: String
    val name: String
    val surname: String
}

data class UserImpl(override val id: String, override val name: String, override val surname: String) : User {
    companion object {
        fun new(id: String, name: String, surname: String) = UserImpl(id, name, surname)
    }
}

data class MongoUser(
    @BsonId val objectId: ObjectId?,
    override val id: String, override val name: String, override val surname: String,
) : User

fun User(id: String, name: String, surname: String) = UserImpl.new(id, name, surname)
