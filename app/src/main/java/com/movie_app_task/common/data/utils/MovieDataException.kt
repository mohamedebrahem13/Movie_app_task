package com.movie_app_task.common.data.utils


open class MovieDataException(message: String?) : Exception(message)

data class NetworkRetrialException(override val message: String?) : MovieDataException(message)

data class NetworkUnhandledException(override val message: String?) : MovieDataException(message)

class UnauthorizedException(
    override val message: String = "Unauthorized Access.") : MovieDataException(message)
data class ClientUnhandledException(
    val httpErrorCode: Int,
    override val message: String?
) : MovieDataException("Unhandled client error with code: $httpErrorCode, and the failure reason: $message")

data class InternalServerErrorException(
    val httpErrorCode: Int,
    override val message: String?
) : MovieDataException("Internal server error with code: $httpErrorCode, and the failure reason: $message")

data class IOErrorException(override val message: String?) : MovieDataException(message)

data class IllegalArgumentExceptionCustom(override val message: String?) : MovieDataException(message)
class DatabaseCorruptionException(message: String?) : MovieDataException(message)

data class UnknownException(override val message: String?) : MovieDataException(message)