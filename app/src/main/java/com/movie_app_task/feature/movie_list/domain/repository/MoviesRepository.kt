package com.movie_app_task.feature.movie_list.domain.repository

import com.movie_app_task.feature.movie_list.domain.models.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun getPopularMoviesLocal(): Flow<List<Movie>>
    suspend fun getPopularMoviesRemote(page: Int= 1): List<Movie>
    fun searchMoviesByNameLocal(query: String): Flow<List<Movie>>
    suspend fun searchMoviesByNameRemote(query: String, page: Int = 1): List<Movie>

}