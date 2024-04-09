package infrastructure.web.handlers

import application.UserService
import application.exceptions.UserAlreadyExists
import domain.User
import infrastructure.web.sendReply
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.util.logging.Level
import java.util.logging.Logger

interface UserHandler {
    val userService: UserService
    fun registerNewUser(context: RoutingContext)
    fun getUser(context: RoutingContext)
}

class UserHandlerImpl(override val userService: UserService) : UserHandler {
    private val logger: Logger = Logger.getLogger("[UserHandler]")
    override fun registerNewUser(context: RoutingContext) {
        logger.log(Level.INFO, "New user registration request")
        context.body().asJsonObject()?.apply {
            logger.log(Level.INFO, encodePrettily())
            val _id: String? = getString("id")
            logger.log(Level.INFO, _id)
            val _name: String? = getString("name")
            val _surname: String? = getString("surname")
            _id?.let { id ->
                _name?.let { name ->
                    _surname?.let { surname ->
                        userService.registerNewUser(id, name, surname)
                    }
                }
            }?.fold(onSuccess = { context.sendReply(JsonObject().put("result", "ok")) }, onFailure = { exception ->
                logger.log(Level.INFO, "Got here: " + exception.message)
                when (exception) {
                    UserAlreadyExists() -> context.sendReply(JsonObject().put("result", "user-id-already-exists"))
                    else -> context.sendReply(JsonObject().put("result", "error-saving-user"))
                }
            }) ?: context.sendReply(JsonObject().put("result", "ERROR: some-fields-were-null"))
        } ?: context.sendReply(JsonObject().put("result", "ERROR: some-fields-were-null"))
    }

    override fun getUser(context: RoutingContext) {
        logger.log(Level.INFO, "Get user request")
        context.apply {
            logger.log(Level.INFO, currentRoute().path)
            val _id: String? = pathParam("userId")
            _id?.let {
                userService.getUser(it)
            }?.onSuccess { user ->
                context.sendReply(
                    JsonObject().put("result", "ok").put("user", user.toJson())
                )
            }?.onFailure {
                context.sendReply(JsonObject().put("result", "user-not-found"))
            } ?: context.sendReply(
                JsonObject().put("result", "ERROR: some-fields-were-null")
            )
        }
    }
}

fun UserHandler(userService: UserService) = UserHandlerImpl(userService)

fun User.toJson(): JsonObject = JsonObject().put("id", this.id).put("name", this.name).put("surname", this.surname)