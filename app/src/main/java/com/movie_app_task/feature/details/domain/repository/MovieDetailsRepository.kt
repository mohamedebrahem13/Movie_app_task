package com.movie_app_task.feature.details.domain.repository

import com.movie_app_task.feature.details.domain.models.MovieDetails

interface MovieDetailsRepository {
    suspend fun getMovieDetailsById(id: Int): MovieDetails
}