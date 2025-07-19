package com.movie_app_task.feature.detiles.domain.repository

import com.movie_app_task.feature.detiles.domain.models.MovieDetails

interface MovieDetailsRepository {
    suspend fun getMovieDetailsById(id: Int): MovieDetails
}