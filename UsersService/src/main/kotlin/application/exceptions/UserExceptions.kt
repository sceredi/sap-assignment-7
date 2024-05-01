package application.exceptions

/**
 * Exception to be thrown when a user already exists in the database
 */
class UserAlreadyExists : Exception()

/**
 * Exception to be thrown when a user is not found in the database
 */
class UserNotFound: Exception()
