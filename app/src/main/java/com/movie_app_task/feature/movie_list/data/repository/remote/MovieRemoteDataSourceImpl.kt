package com.movie_app_task.feature.movie_list.data.repository.remote

import com.movie_app_task.common.data.repository.remote.MoviesApiService
import com.movie_app_task.feature.movie_list.data.models.dto.MoviesResponseDto
import com.movie_app_task.feature.movie_list.domain.repository.remote.MovieRemoteDataSource

class MovieRemoteDataSourceImpl(
    private val apiService: MoviesApiService
) : MovieRemoteDataSource {
    override suspend fun getPopularMovies(page: Int): MoviesResponseDto =
        apiService.getPopularMovies(page)


    override suspend fun searchMoviesByQuery(
        query: String,
        page: Int
    ): MoviesResponseDto = apiService.searchMoviesByQuery(page = page, query = query)

}