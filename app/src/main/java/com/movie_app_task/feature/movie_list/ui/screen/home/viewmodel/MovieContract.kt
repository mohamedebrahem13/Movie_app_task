package com.movie_app_task.feature.movie_list.ui.screen.home.viewmodel

import com.movie_app_task.common.domain.utils.MovieDomainException
import com.movie_app_task.feature.movie_list.domain.models.Movie

interface MovieContract {

    sealed class MovieAction {
        object LoadMovies : MovieAction()
        data class OnMovieClick(val movieId: Int) : MovieAction()

        object OnVoiceSearchClick : MovieAction()

        data class OnSearchQueryChange(val query: String) : MovieAction()
        object OnVoiceSearchFinished : MovieAction()
        data class OnPermissionResult(val granted: Boolean) : MovieAction()
    }

    sealed class MovieEvent {
        data class ShowError(val error: MovieDomainException) : MovieEvent()
        data class Navigate(val movieId: Int) : MovieEvent()
    }

    data class MovieState(
        val movies: List<Movie> = emptyList(),
        val isLoading: Boolean = false,
        val error: MovieDomainException? = null,
        val isVoiceRecording: Boolean = false,
        val isPermissionGranted: Boolean = false,
        val searchQuery: String = "",
        val isFromLocal: Boolean = false

    )
}