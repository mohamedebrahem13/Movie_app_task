package com.movie_app_task.feature.detiles.domain.usecase

import com.movie_app_task.common.domain.utils.MovieDomainException
import com.movie_app_task.common.domain.utils.Resource
import com.movie_app_task.common.domain.utils.UnknownException
import com.movie_app_task.feature.detiles.domain.models.MovieDetails
import com.movie_app_task.feature.detiles.domain.repository.MovieDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMovieDetailsByIdUseCase(
    private val repository: MovieDetailsRepository
) {
    operator fun invoke(id: Int): Flow<Resource<MovieDetails>> = flow {
        try {
            val movieDetails = repository.getMovieDetailsById(id)
            emit(Resource.Success(movieDetails))
        } catch (e: Exception) {
            val domainException = when (e) {
                is MovieDomainException -> e
                else -> UnknownException(e.message ?: "Unknown error from local DB")
            }
            emit(Resource.Failure(domainException))
        }
    }
}