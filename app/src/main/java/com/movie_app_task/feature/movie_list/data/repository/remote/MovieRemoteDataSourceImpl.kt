package com.movie_app_task.feature.movie_list.data.repository.remote

import com.movie_app_task.common.Constants
import com.movie_app_task.common.domain.repository.remote.INetworkProvider
import com.movie_app_task.feature.movie_list.data.models.dto.MoviesResponseDto
import com.movie_app_task.feature.movie_list.domain.repository.remote.MovieRemoteDataSource

class MovieRemoteDataSourceImpl(
    private val networkProvider: INetworkProvider
) : MovieRemoteDataSource {

    override suspend fun getPopularMovies(page: Int): MoviesResponseDto {
        return networkProvider.get(
            responseWrappedModel = MoviesResponseDto::class.java,
            pathUrl = Constants.DISCOVER_MOVIE_ENDPOINT,
            queryParams = mapOf("page" to page)
        )
    }

    override suspend fun searchMoviesByQuery(query: String, page: Int): MoviesResponseDto {
        return networkProvider.get(
            responseWrappedModel = MoviesResponseDto::class.java,
            pathUrl = Constants.SEARCH_MOVIE_ENDPOINT,
            queryParams = mapOf("query" to query, "page" to page)
        )
    }
}