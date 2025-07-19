package com.movie_app_task.common.data.repository.remote.converter

import com.movie_app_task.common.data.utils.MovieDataException
import okhttp3.ResponseBody

interface IExceptionConverter {
    fun convertNetworkExceptions(throwable: Throwable): MovieDataException
    fun convertResponseExceptions(code: Int, errorBody: ResponseBody?): MovieDataException

}