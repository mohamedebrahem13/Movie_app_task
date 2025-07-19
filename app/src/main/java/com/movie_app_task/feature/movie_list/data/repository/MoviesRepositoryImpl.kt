package com.movie_app_task.feature.movie_list.data.repository

import com.movie_app_task.common.data.utils.safeFlow
import com.movie_app_task.common.domain.utils.safeCall
import com.movie_app_task.feature.movie_list.data.mapper.MoviesMapper
import com.movie_app_task.feature.movie_list.domain.models.Movie
import com.movie_app_task.feature.movie_list.domain.repository.MoviesRepository
import com.movie_app_task.feature.movie_list.domain.repository.local.MovieLocalDataSource
import com.movie_app_task.feature.movie_list.domain.repository.remote.MovieRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class MoviesRepositoryImpl(
    private val moviesRemoteDataSource: MovieRemoteDataSource,
    private val moviesLocalDataSource: MovieLocalDataSource
) : MoviesRepository {

    override fun getPopularMoviesLocal(): Flow<List<Movie>> {
        return moviesLocalDataSource.getMovies()
            .map { entities -> MoviesMapper.entityToDomain(entities) }
    }

    override suspend fun getPopularMoviesRemote(page: Int): List<Movie> {
        return safeCall {
            val remoteMovies = moviesRemoteDataSource.getPopularMovies(page)
            val domainMovies = MoviesMapper.dtoToDomain(remoteMovies)
            val entities = MoviesMapper.dtoListToEntityList(remoteMovies.results ?: emptyList())
            moviesLocalDataSource.insertMovies(entities)
            domainMovies
        }
    }

    override fun searchMoviesByName(query: String): Flow<List<Movie>> {
        return safeFlow {
            moviesLocalDataSource.searchMoviesByName(query)
        }.map { entities ->
            MoviesMapper.entityToDomain(entities)
        }
    }
}