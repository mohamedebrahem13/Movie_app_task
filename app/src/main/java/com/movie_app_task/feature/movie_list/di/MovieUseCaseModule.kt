package com.movie_app_task.feature.movie_list.di
import com.movie_app_task.feature.movie_list.domain.repository.MoviesRepository
import com.movie_app_task.feature.movie_list.domain.usecase.GetPopularMoviesLocalUseCase
import com.movie_app_task.feature.movie_list.domain.usecase.GetPopularMoviesRemoteUseCase
import com.movie_app_task.feature.movie_list.domain.usecase.SearchMoviesByNameUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object MovieUseCaseModule {

    @Provides
    @Singleton
    fun provideGetPopularMoviesUseCase(
        moviesRepository: MoviesRepository
    ): GetPopularMoviesRemoteUseCase = GetPopularMoviesRemoteUseCase(moviesRepository)
    @Provides
    @Singleton
    fun provideGetPopularMoviesLocalUseCase(
        moviesRepository: MoviesRepository
    ): GetPopularMoviesLocalUseCase = GetPopularMoviesLocalUseCase(moviesRepository)
    @Provides
    @Singleton
    fun provideSearchMoviesByNameUseCase(
        moviesRepository: MoviesRepository
    ): SearchMoviesByNameUseCase = SearchMoviesByNameUseCase(moviesRepository)

}