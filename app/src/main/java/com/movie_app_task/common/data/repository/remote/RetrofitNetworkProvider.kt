package com.movie_app_task.common.data.repository.remote

import com.movie_app_task.common.data.utils.getModelFromJSON
import com.movie_app_task.common.domain.remote.INetworkProvider
import java.lang.reflect.Type

class RetrofitNetworkProvider (
    private val moviesApiService: MoviesApiService
) : INetworkProvider {

    override suspend fun <ResponseBody> get(
        responseWrappedModel: Type,
        pathUrl: String,
        headers: Map<String, Any>?,
        queryParams: Map<String, Any>?
    ): ResponseBody {
        val response = moviesApiService.get(
            pathUrl = pathUrl,
            headers = headers ?: emptyMap(),
            queryParams = queryParams ?: emptyMap()
        )
        return response.string().getModelFromJSON(responseWrappedModel)
    }
}