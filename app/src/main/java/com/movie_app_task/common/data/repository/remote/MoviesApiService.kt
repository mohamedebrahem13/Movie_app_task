package com.movie_app_task.common.data.repository.remote

import com.movie_app_task.common.Constants
import com.movie_app_task.feature.movie_list.data.models.dto.MoviesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApiService {

    @GET(Constants.DISCOVER_MOVIE_ENDPOINT)
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1
    ): MoviesResponseDto

    @GET(Constants.SEARCH_MOVIE_ENDPOINT)
    suspend fun searchMoviesByQuery(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): MoviesResponseDto
}