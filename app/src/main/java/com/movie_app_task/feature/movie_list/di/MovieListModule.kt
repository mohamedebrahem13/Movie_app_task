package com.movie_app_task.feature.movie_list.di

import com.movie_app_task.feature.movie_list.data.repository.local.MovieLocalDataSourceImpl
import com.movie_app_task.common.data.repository.local.db.MovieDao
import com.movie_app_task.feature.movie_list.data.repository.remote.MovieRemoteDataSourceImpl
import com.movie_app_task.common.domain.remote.INetworkProvider
import com.movie_app_task.feature.movie_list.data.repository.MoviesRepositoryImpl
import com.movie_app_task.feature.movie_list.domain.repository.MoviesRepository
import com.movie_app_task.feature.movie_list.domain.repository.local.MovieLocalDataSource
import com.movie_app_task.feature.movie_list.domain.repository.remote.MovieRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieListModule {

    @Provides
    @Singleton
    fun provideMovieLocalDataSource(
        movieDao: MovieDao
    ): MovieLocalDataSource = MovieLocalDataSourceImpl(movieDao)

    @Provides
    @Singleton
    fun provideMovieRemoteDataSource(
        networkProvider: INetworkProvider
    ): MovieRemoteDataSource = MovieRemoteDataSourceImpl(networkProvider)
    @Provides
    @Singleton
    fun provideMoviesRepository(
        remoteDataSource: MovieRemoteDataSource,
        localDataSource: MovieLocalDataSource
    ): MoviesRepository = MoviesRepositoryImpl(remoteDataSource, localDataSource)
}