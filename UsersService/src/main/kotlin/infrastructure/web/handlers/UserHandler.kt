package infrastructure.web.handlers

import application.UserService
import application.exceptions.UserAlreadyExists
import domain.User
import infrastructure.web.sendReply
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.system.exitProcess

/**
 * Represents the user handler
 */
interface UserHandler {
    /**
     * User service to interact with the application
     */
    val userService: UserService

    /**
     * Register a new user
     */
    fun registerNewUser(context: RoutingContext)

    /**
     * Get a user by its id
     */
    fun getUser(context: RoutingContext)


    /**
     * Kills the service, only used for testing
     */
    fun kill(context: RoutingContext)

    /**
     * Answers prometheus for the metrics
     */
    fun metrics(context: RoutingContext, counter: Int)
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

    override fun kill(context: RoutingContext) {
        exitProcess(1)
    }

    override fun metrics(context: RoutingContext, counter: Int) {
        val ans = """
            # HELP requests_total The total number of received requests
            # TYPE requests_total counter
            requests_total $counter
        """.trimIndent()
        context.response().putHeader("content-type", "text-plain").end(ans)
    }
}

/**
 * Factory function to create a UserHandler
 * @param userService User service to interact with the application
 * @return UserHandler
 */
fun UserHandler(userService: UserService) = UserHandlerImpl(userService)

/**
 * Converts a user to a JSON object
 */
fun User.toJson(): JsonObject = JsonObject().put("id", this.id).put("name", this.name).put("surname", this.surname)