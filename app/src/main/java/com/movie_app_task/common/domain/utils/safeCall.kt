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
        is NetworkRetrialException -> NetworkException(e.message)
        is NetworkUnhandledException -> UnknownException(e.message)
        is InternalServerErrorException -> ServerErrorException(e.message)
        is ClientUnhandledException,
        is IllegalArgumentExceptionCustom -> DataReadWriteException(e.message)

        is UnauthorizedException -> UserNotAuthenticatedException(e.message)
        is IOErrorException,
        is DatabaseCorruptionException -> DataReadWriteException(e.message)

        else -> UnknownException(e.message)
    }

}