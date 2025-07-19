package com.movie_app_task.feature.movie_list.domain.usecase

import com.movie_app_task.common.domain.utils.MovieDomainException
import com.movie_app_task.common.domain.utils.Resource
import com.movie_app_task.common.domain.utils.UnknownException
import com.movie_app_task.feature.movie_list.domain.models.Movie
import com.movie_app_task.feature.movie_list.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPopularMoviesRemoteUseCase(
    private val moviesRepository: MoviesRepository
) {
    operator fun invoke(): Flow<Resource<List<Movie>>> = flow {
        try {
            val remoteMovies = moviesRepository.getPopularMoviesRemote()
            emit(Resource.Success(remoteMovies))
        } catch (e: Exception) {
            val domainException = when (e) {
                is MovieDomainException -> e
                else -> UnknownException(e.message ?: "Unknown error")
            }
            emit(Resource.Failure(domainException))
        }
    }
}