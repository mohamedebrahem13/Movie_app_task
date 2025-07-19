package com.movie_app_task.feature.movie_list.data.repository.local

import com.movie_app_task.common.data.repository.local.db.MovieDao
import com.movie_app_task.common.data.utils.safeCall
import com.movie_app_task.common.data.utils.safeFlow
import com.movie_app_task.feature.movie_list.data.models.entity.MovieEntity
import com.movie_app_task.feature.movie_list.domain.repository.local.MovieLocalDataSource
import kotlinx.coroutines.flow.Flow

class MovieLocalDataSourceImpl(
    private val movieDao: MovieDao
) : MovieLocalDataSource {

    override suspend fun insertMovies(movies: List<MovieEntity>) {
        safeCall {
            movieDao.insertMovies(movies)
        }
    }

    override suspend fun clearMovies() {
        safeCall {
            movieDao.clearAllMovies()
        }
    }

    override fun getMovies(): Flow<List<MovieEntity>> {
        return safeFlow {
            movieDao.getPagedMovies()
        }
    }

    override fun searchMoviesByName(query: String): Flow<List<MovieEntity>> {
        return safeFlow {
            movieDao.searchMoviesByName(query)
        }
    }
}