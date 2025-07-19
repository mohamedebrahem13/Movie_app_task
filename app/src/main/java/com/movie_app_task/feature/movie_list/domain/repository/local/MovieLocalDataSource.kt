package com.movie_app_task.feature.movie_list.domain.repository.local

import com.movie_app_task.feature.movie_list.data.models.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

interface MovieLocalDataSource {
    suspend fun insertMovies(movies: List<MovieEntity>)
    suspend fun clearMovies()
    fun getMovies():  Flow<List<MovieEntity>>
    fun searchMoviesByName(query: String): Flow<List<MovieEntity>>

}