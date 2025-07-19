package com.movie_app_task.feature.detiles.di

import com.movie_app_task.common.data.repository.local.db.MovieDao
import com.movie_app_task.feature.detiles.data.repository.MovieDetailsRepositoryImpl
import com.movie_app_task.feature.detiles.data.repository.local.MovieDetailsLocalDataSourceImpl
import com.movie_app_task.feature.detiles.domain.repository.MovieDetailsRepository
import com.movie_app_task.feature.detiles.domain.repository.local.MovieDetailsLocalDataSource
import com.movie_app_task.feature.detiles.domain.usecase.GetMovieDetailsByIdUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MovieDetailsModule {
    @Provides
    fun provideMovieDetailsLocalDataSource(
        movieDao: MovieDao
    ): MovieDetailsLocalDataSource = MovieDetailsLocalDataSourceImpl(movieDao)

    @Provides
    fun provideMovieDetailsRepository(
        localDataSource: MovieDetailsLocalDataSource
    ): MovieDetailsRepository = MovieDetailsRepositoryImpl(localDataSource)
    @Provides
    fun provideGetMovieDetailsByIdUseCase(
        repository: MovieDetailsRepository
    ): GetMovieDetailsByIdUseCase = GetMovieDetailsByIdUseCase(repository)

}