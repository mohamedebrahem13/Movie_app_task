package com.movie_app_task.feature.detiles.domain.repository.local

import com.movie_app_task.feature.movie_list.data.models.entity.MovieEntity

interface MovieDetailsLocalDataSource {
    suspend fun getMovieDetails(id: Int): MovieEntity
}