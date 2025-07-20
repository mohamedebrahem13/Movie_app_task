package com.movie_app_task.common.domain.utils

import com.movie_app_task.common.data.utils.ClientUnhandledException
import com.movie_app_task.common.data.utils.DatabaseCorruptionException
import com.movie_app_task.common.data.utils.IOErrorException
import com.movie_app_task.common.data.utils.IllegalArgumentExceptionCustom
import com.movie_app_task.common.data.utils.InternalServerErrorException
import com.movie_app_task.common.data.utils.MovieDataException
import com.movie_app_task.common.data.utils.NetworkRetrialException
import com.movie_app_task.common.data.utils.NetworkUnhandledException
import com.movie_app_task.common.data.utils.UnauthorizedException

suspend fun <T> safeCall(block: suspend () -> T): T {
    return try {
        block()
    } catch (e: MovieDataException) {
        throw mapToDomainException(e)
    }
}

fun mapToDomainException(e: Throwable): MovieDomainException {
    return when (e) {
        is NetworkRetrialException -> NetworkException("Network issue occurred. Please try again.")
        is NetworkUnhandledException -> UnknownException("Unexpected network error.")
        is InternalServerErrorException -> ServerErrorException("Something went wrong on the server.")

        is ClientUnhandledException,
        is IllegalArgumentExceptionCustom -> DataReadWriteException("Invalid data encountered.")

        is UnauthorizedException -> UserNotAuthenticatedException("You are not authorized. Please login again.")
        is IOErrorException,
        is DatabaseCorruptionException -> DataReadWriteException("Problem reading from storage.")

        else -> UnknownException("An unknown error occurred.")
    }
}