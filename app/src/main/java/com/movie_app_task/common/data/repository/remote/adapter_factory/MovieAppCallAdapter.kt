package com.movie_app_task.common.data.repository.remote.adapter_factory

import com.movie_app_task.common.data.repository.remote.converter.IExceptionConverter
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class MovieAppCallAdapter(
    private val exceptionConverter: IExceptionConverter
) : CallAdapter<ResponseBody, Call<ResponseBody>> {

    override fun responseType(): Type {
        return ResponseBody::class.java
    }

    override fun adapt(call: Call<ResponseBody>): Call<ResponseBody> {
        return MovieAppCall(call, exceptionConverter)
    }
}