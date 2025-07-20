package com.movie_app_task.feature.details.di

import com.movie_app_task.common.data.repository.local.db.MovieDao
import com.movie_app_task.feature.details.data.repository.MovieDetailsRepositoryImpl
import com.movie_app_task.feature.details.data.repository.local.MovieDetailsLocalDataSourceImpl
import com.movie_app_task.feature.details.domain.repository.MovieDetailsRepository
import com.movie_app_task.feature.details.domain.repository.local.MovieDetailsLocalDataSource
import com.movie_app_task.feature.details.domain.usecase.GetMovieDetailsByIdUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
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