package com.movie_app_task.feature.movie_list.ui.screen.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.movie_app_task.common.domain.utils.InvalidSearchQueryException
import com.movie_app_task.common.domain.utils.Resource
import com.movie_app_task.common.ui.BaseMovieViewModel
import com.movie_app_task.feature.movie_list.domain.usecase.GetPopularMoviesLocalUseCase
import com.movie_app_task.feature.movie_list.domain.usecase.GetPopularMoviesRemoteUseCase
import com.movie_app_task.feature.movie_list.domain.usecase.SearchMoviesByNameRemoteUseCase
import com.movie_app_task.feature.movie_list.domain.usecase.SearchMoviesByNameLocalUseCase
import com.movie_app_task.feature.movie_list.domain.usecase.ValidateSearchQueryUseCase
import com.movie_app_task.feature.movie_list.ui.screen.home.viewmodel.MovieContract.MovieEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val getPopularMoviesRemoteUseCase: GetPopularMoviesRemoteUseCase,
    private val getPopularMoviesLocalUseCase: GetPopularMoviesLocalUseCase,
    private val searchMoviesByNameLocalUseCase: SearchMoviesByNameLocalUseCase,
    private val searchMoviesByNameRemoteUseCase: SearchMoviesByNameRemoteUseCase,
    private val validateSearchQueryUseCase: ValidateSearchQueryUseCase
) : BaseMovieViewModel<MovieContract.MovieAction, MovieContract.MovieEvent, MovieContract.MovieState>(
    MovieContract.MovieState()
) {
    private var debounceJob: Job? = null

    override fun onActionTrigger(action: MovieContract.MovieAction?) {
        when (action) {
            is MovieContract.MovieAction.LoadMovies -> loadMovies()
            is MovieContract.MovieAction.OnMovieClick -> {
                sendEvent(Navigate(action.movieId, action.movieTitle))
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

            // Fetch local movies first
            launch(Dispatchers.IO) {
                getPopularMoviesLocalUseCase().collect { localResult ->
                    when (localResult) {
                        is Resource.Success -> {
                            setState(
                                currentState.copy(
                                    movies = localResult.model,
                                    isLoading = false,
                                    error = null,
                                    isFromLocal = true
                                )
                            )
                        }

                        is Resource.Failure -> {
                            // If local fails, still try remote
                            setState(
                                currentState.copy(
                                    isLoading = false,
                                    error = localResult.exception,
                                    isFromLocal = true
                                )
                            )
                        }
                    }
                }
            }

            // Fetch remote in parallel
            launch(Dispatchers.IO) {
                getPopularMoviesRemoteUseCase().collect { remoteResult ->
                    when (remoteResult) {
                        is Resource.Success -> {
                            setState(
                                currentState.copy(
                                    movies = remoteResult.model,
                                    isLoading = false,
                                    error = null,
                                    isFromLocal = false
                                )
                            )
                        }

                        is Resource.Failure -> {
                            sendEvent(ShowError(remoteResult.exception))
                            setState(currentState.copy(isLoading = false))
                        }
                    }
                }
            }
        }
    }
    private fun onSearchQueryChange(query: String) {
        setState(currentState.copy(searchQuery = query, isLoading = true, error = null))
        debounceJob?.cancel()

        if (query.isBlank()) {
            debounceJob = null
            loadMovies()
            return
        }
        try {
            validateSearchQueryUseCase(query)
        } catch (e: InvalidSearchQueryException) {
            setState(
                currentState.copy(
                    isLoading = false,
                    error = e
                )
            )
            return
        }

        debounceJob = viewModelScope.launch {
            delay(1500)

            searchMoviesByNameLocalUseCase(query).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val localResults = result.model

                        if (localResults.isNotEmpty()) {
                            setState(
                                currentState.copy(
                                    movies = localResults,
                                    isLoading = false,
                                    error = null,
                                    isFromLocal = true
                                )
                            )
                        } else {

                            setState(currentState.copy(isLoading = true, isFromLocal = false))

                            searchMoviesByNameRemoteUseCase(query).collect { remoteResult ->
                                when (remoteResult) {
                                    is Resource.Success -> {
                                        setState(
                                            currentState.copy(
                                                movies = remoteResult.model,
                                                isLoading = false,
                                                error = null,
                                                isFromLocal = false
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