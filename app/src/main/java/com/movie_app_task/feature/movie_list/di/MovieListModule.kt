package com.movie_app_task.feature.movie_list.di

import com.movie_app_task.feature.movie_list.data.repository.local.MovieLocalDataSourceImpl
import com.movie_app_task.common.data.repository.local.db.MovieDao
import com.movie_app_task.feature.movie_list.data.repository.remote.MovieRemoteDataSourceImpl
import com.movie_app_task.common.domain.repository.remote.INetworkProvider
import com.movie_app_task.feature.movie_list.data.repository.MoviesRepositoryImpl
import com.movie_app_task.feature.movie_list.domain.repository.MoviesRepository
import com.movie_app_task.feature.movie_list.domain.repository.local.MovieLocalDataSource
import com.movie_app_task.feature.movie_list.domain.repository.remote.MovieRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object MovieListModule {

    @Provides
    fun provideMovieLocalDataSource(
        movieDao: MovieDao
    ): MovieLocalDataSource = MovieLocalDataSourceImpl(movieDao)

    @Provides
    fun provideMovieRemoteDataSource(
        networkProvider: INetworkProvider
    ): MovieRemoteDataSource = MovieRemoteDataSourceImpl(networkProvider)
    @Provides
    fun provideMoviesRepository(
        remoteDataSource: MovieRemoteDataSource,
        localDataSource: MovieLocalDataSource
    ): MoviesRepository = MoviesRepositoryImpl(remoteDataSource, localDataSource)
}