package com.movie_app_task.feature.movie_list.domain.usecase

import com.movie_app_task.common.domain.utils.MovieDomainException
import com.movie_app_task.common.domain.utils.Resource
import com.movie_app_task.common.domain.utils.UnknownException
import com.movie_app_task.feature.movie_list.domain.models.Movie
import com.movie_app_task.feature.movie_list.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPopularMoviesLocalUseCase(
    private val repository: MoviesRepository
) {
    operator fun invoke(): Flow<Resource<List<Movie>>> = flow {
        try {
            repository.getPopularMoviesLocal().collect { movies ->
                emit(Resource.Success(movies))
            }
        } catch (e: Exception) {
            val domainException = when (e) {
                is MovieDomainException -> e
                else -> UnknownException(e.message ?: "Unknown error from local DB")
            }
            emit(Resource.Failure(domainException))
        }
    }
}