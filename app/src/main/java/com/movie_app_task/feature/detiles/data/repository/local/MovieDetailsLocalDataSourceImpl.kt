package com.movie_app_task.feature.detiles.data.repository.local

import com.movie_app_task.common.data.repository.local.db.MovieDao
import com.movie_app_task.common.data.utils.safeCall
import com.movie_app_task.feature.detiles.domain.repository.local.MovieDetailsLocalDataSource
import com.movie_app_task.feature.movie_list.data.models.entity.MovieEntity

class MovieDetailsLocalDataSourceImpl(
    private val movieDao: MovieDao
) : MovieDetailsLocalDataSource {

    override suspend fun getMovieDetails(id: Int): MovieEntity {
        return safeCall {
            movieDao.getMovieById(id)
        }
    }
}