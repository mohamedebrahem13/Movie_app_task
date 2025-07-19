package com.movie_app_task.feature.detiles.data.repository

import com.movie_app_task.common.domain.utils.safeCall
import com.movie_app_task.feature.detiles.data.mapper.MovieDetailsMapper
import com.movie_app_task.feature.detiles.domain.models.MovieDetails
import com.movie_app_task.feature.detiles.domain.repository.MovieDetailsRepository
import com.movie_app_task.feature.detiles.domain.repository.local.MovieDetailsLocalDataSource

class MovieDetailsRepositoryImpl(
    private val localDataSource: MovieDetailsLocalDataSource
) : MovieDetailsRepository {

    override suspend fun getMovieDetailsById(id: Int): MovieDetails {
        return safeCall {
            val localMovie = localDataSource.getMovieDetails(id)
                MovieDetailsMapper.entityToDomain(localMovie)
            }
        }
    }
