package com.movie_app_task.feature.movie_list.ui.screen.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.movie_app_task.common.domain.utils.Resource
import com.movie_app_task.common.ui.BaseMovieViewModel
import com.movie_app_task.feature.movie_list.domain.usecase.GetPopularMoviesLocalUseCase
import com.movie_app_task.feature.movie_list.domain.usecase.GetPopularMoviesRemoteUseCase
import com.movie_app_task.feature.movie_list.domain.usecase.SearchMoviesByNameUseCase
import com.movie_app_task.feature.movie_list.ui.screen.home.viewmodel.MovieContract.MovieEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val getPopularMoviesRemoteUseCase: GetPopularMoviesRemoteUseCase,
    private val getPopularMoviesLocalUseCase: GetPopularMoviesLocalUseCase,
    private val searchMoviesByNameUseCase: SearchMoviesByNameUseCase
) : BaseMovieViewModel<MovieContract.MovieAction, MovieContract.MovieEvent, MovieContract.MovieState>(
    MovieContract.MovieState()
) {
    private var debounceJob: Job? = null

    override fun onActionTrigger(action: MovieContract.MovieAction?) {
        when (action) {
            is MovieContract.MovieAction.LoadMovies -> loadMovies()
            is MovieContract.MovieAction.OnMovieClick -> {
                sendEvent(Navigate(action.movieId))
            }

            is MovieContract.MovieAction.OnPermissionResult -> handlePermissionResult(action.granted)
            is MovieContract.MovieAction.OnSearchQueryChange -> onSearchQueryChange(action.query)
            is MovieContract.MovieAction.OnVoiceSearchFinished -> onVoiceSearchFinished()
            is MovieContract.MovieAction.OnVoiceSearchClick -> handleVoiceSearchClick()
            else -> Unit
        }
    }

    private fun loadMovies() {
        viewModelScope.launch {
            setState(currentState.copy(isLoading = true, error = null))

            launch {
                getPopularMoviesLocalUseCase().collect { localResult ->
                    when (localResult) {
                        is Resource.Success -> {
                            setState(
                                currentState.copy(
                                    movies = localResult.model,
                                    isLoading = false,
                                    error = null
                                )
                            )
                        }

                        is Resource.Failure -> {
                            setState(
                                currentState.copy(
                                    error = localResult.exception,
                                    isLoading = false
                                )
                            )
                        }
                    }
                }
            }

            launch {
                getPopularMoviesRemoteUseCase().collect { remoteResult ->
                    when (remoteResult) {
                        is Resource.Success -> {
                            setState(
                                currentState.copy(
                                    movies = remoteResult.model,
                                    isLoading = false,
                                    error = null
                                )
                            )
                        }

                        is Resource.Failure -> {
                            setState(
                                currentState.copy(
                                    error = remoteResult.exception,
                                    isLoading = false
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onSearchQueryChange(query: String) {
        setState(currentState.copy(searchQuery = query))
        debounceJob?.cancel()

        if (query.isBlank()) {
            debounceJob = null
            loadMovies()
            return
        }

        debounceJob = viewModelScope.launch {
            delay(1500)

            searchMoviesByNameUseCase(query).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        setState(
                            currentState.copy(
                                movies = result.model,
                                isLoading = false,
                                error = null
                            )
                        )
                    }
                    is Resource.Failure -> {
                        setState(
                            currentState.copy(
                                error = result.exception,
                                isLoading = false
                            )
                        )
                    }
                }
            }
        }
    }
    private fun handlePermissionResult(granted: Boolean) {
        setState(currentState.copy(isPermissionGranted = granted))
    }

    private fun onVoiceSearchFinished() {
        setState(currentState.copy(isVoiceRecording = false))
    }

    private fun handleVoiceSearchClick() {
        setState(currentState.copy(isVoiceRecording = true))
    }

    override fun clearState() {
        setState(MovieContract.MovieState())
    }
}