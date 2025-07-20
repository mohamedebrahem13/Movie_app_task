package com.movie_app_task.common.data.repository.remote

import okhttp3.ResponseBody
import retrofit2.http.*

interface MoviesApiService {
    @GET("{path}")
    @JvmSuppressWildcards
    suspend fun get(
        @Path("path") pathUrl: String,
        @QueryMap queryParams: Map<String, Any>,
        @HeaderMap headers: Map<String, Any>,
    ): ResponseBody
}