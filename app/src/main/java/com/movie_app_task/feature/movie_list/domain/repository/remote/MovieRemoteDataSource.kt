package com.movie_app_task.feature.movie_list.domain.repository.remote

import com.movie_app_task.feature.movie_list.data.models.dto.MoviesResponseDto

interface MovieRemoteDataSource {
    suspend fun getPopularMovies(page: Int = 1): MoviesResponseDto
}