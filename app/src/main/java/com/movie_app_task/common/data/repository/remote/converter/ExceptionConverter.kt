package com.movie_app_task.common.data.repository.remote.converter

import com.google.gson.JsonSyntaxException
import com.movie_app_task.common.data.utils.ClientUnhandledException
import com.movie_app_task.common.data.utils.IOErrorException
import com.movie_app_task.common.data.utils.InternalServerErrorException
import com.movie_app_task.common.data.utils.MovieDataException
import com.movie_app_task.common.data.utils.NetworkRetrialException
import com.movie_app_task.common.data.utils.NetworkUnhandledException
import com.movie_app_task.common.data.utils.UnauthorizedException
import okhttp3.ResponseBody
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ExceptionConverter : IExceptionConverter {
    override fun convertNetworkExceptions(throwable: Throwable): MovieDataException {
        return when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is IOException -> {
                NetworkRetrialException(
                    message = "Retrial network error."
                )
            }

            is JsonSyntaxException -> {
                IOErrorException(
                    message = "Invalid JSON format. Please check the data structure."
                )
            }

            else -> {
                NetworkUnhandledException(
                    message = "NetworkException Unhandled error."
                )
            }
        }
    }

    override fun convertResponseExceptions(
        code: Int,
        errorBody: ResponseBody?
    ): MovieDataException {
        val errorMessage = errorBody?.string()

        return when (code) {
            401 -> UnauthorizedException()

            in 400..499 -> ClientUnhandledException(
                httpErrorCode = code,
                message = errorMessage ?: "Client error without message."
            )

            in 500..599 -> InternalServerErrorException(
                httpErrorCode = code,
                message = errorMessage
            )

            else -> ClientUnhandledException(
                httpErrorCode = code,
                message = errorMessage
            )
        }
    }
}