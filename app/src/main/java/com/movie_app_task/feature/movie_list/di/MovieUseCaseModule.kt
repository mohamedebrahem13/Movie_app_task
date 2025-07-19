package com.movie_app_task.feature.movie_list.di
import com.movie_app_task.feature.movie_list.domain.repository.MoviesRepository
import com.movie_app_task.feature.movie_list.domain.usecase.GetPopularMoviesLocalUseCase
import com.movie_app_task.feature.movie_list.domain.usecase.GetPopularMoviesRemoteUseCase
import com.movie_app_task.feature.movie_list.domain.usecase.SearchMoviesByNameRemoteUseCase
import com.movie_app_task.feature.movie_list.domain.usecase.SearchMoviesByNameLocalUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object MovieUseCaseModule {

    @Provides
    fun provideGetPopularMoviesUseCase(
        moviesRepository: MoviesRepository
    ): GetPopularMoviesRemoteUseCase = GetPopularMoviesRemoteUseCase(moviesRepository)
    @Provides
    fun provideGetPopularMoviesLocalUseCase(
        moviesRepository: MoviesRepository
    ): GetPopularMoviesLocalUseCase = GetPopularMoviesLocalUseCase(moviesRepository)
    @Provides
    fun provideSearchMoviesByNameUseCase(
        moviesRepository: MoviesRepository
    ): SearchMoviesByNameLocalUseCase = SearchMoviesByNameLocalUseCase(moviesRepository)
    @Provides
    fun provideSearchMoviesByNameRemoteUseCase(
        moviesRepository: MoviesRepository
    ): SearchMoviesByNameRemoteUseCase = SearchMoviesByNameRemoteUseCase(moviesRepository)


}