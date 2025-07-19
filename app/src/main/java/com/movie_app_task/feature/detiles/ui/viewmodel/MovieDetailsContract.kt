package com.movie_app_task.feature.detiles.ui.viewmodel

import com.movie_app_task.common.domain.utils.MovieDomainException
import com.movie_app_task.feature.detiles.domain.models.MovieDetails

interface MovieDetailsContract {

    sealed class Action {
        data class LoadMovieDetails(val movieId: Int) : Action()
        object OnBackClicked : Action()

    }

    sealed class State {
        object Idle : State()
        object Loading : State()
        data class Success(val movie: MovieDetails) : State()
        data class Failure(val error: MovieDomainException) : State()
    }

    sealed class Event {
        object NavigateBack : Event()
        data class ShowError(val error: MovieDomainException) : Event()
    }
}