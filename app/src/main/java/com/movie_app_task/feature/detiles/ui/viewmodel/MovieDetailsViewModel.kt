package com.movie_app_task.feature.detiles.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.movie_app_task.common.domain.utils.Resource
import com.movie_app_task.common.ui.BaseMovieViewModel
import com.movie_app_task.feature.detiles.domain.usecase.GetMovieDetailsByIdUseCase
import com.movie_app_task.feature.detiles.ui.viewmodel.MovieDetailsContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsByIdUseCase: GetMovieDetailsByIdUseCase
) : BaseMovieViewModel<Action, Event, State>(State.Idle) {

    override fun onActionTrigger(action: Action?) {
        when (action) {
            is Action.LoadMovieDetails -> loadMovieDetails(action.movieId)
            else -> Unit
        }
    }

    private fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            setState(State.Loading)
            getMovieDetailsByIdUseCase(movieId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        setState(State.Success(result.model))
                    }
                    is Resource.Failure -> {
                        setState(State.Failure(result.exception))
                        sendEvent(Event.ShowError(result.exception))
                    }
                }
            }
        }
    }

    override fun clearState() {
        setState(State.Idle)
    }
}