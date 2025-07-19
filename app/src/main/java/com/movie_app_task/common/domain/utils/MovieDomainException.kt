package com.movie_app_task.common.domain.utils

open class MovieDomainException(message: String? = null) : Exception(message)

class NetworkException(message: String? = null) : MovieDomainException(message)
class ServerErrorException(message: String? = null) : MovieDomainException(message)
class UserNotAuthenticatedException(message: String? = null) : MovieDomainException(message)
class UnknownException(message: String? = null) : MovieDomainException(message)
class DataReadWriteException(message: String? = null) : MovieDomainException(message)
class InvalidSearchQueryException(message: String?= null) : MovieDomainException(message)
