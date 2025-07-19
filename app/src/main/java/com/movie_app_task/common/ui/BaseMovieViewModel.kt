package com.movie_app_task.common.ui

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseMovieViewModel<Action, Event, State>(
    initialState: State
) : ViewModel() {

    private val _viewState = MutableStateFlow(initialState)
    val viewState: StateFlow<State> = _viewState
    val currentState: State get() = viewState.value

    // Channel for one-time UI events
    private val eventChannel = Channel<Event>(Channel.UNLIMITED)
    val singleEvent: Flow<Event> = eventChannel.receiveAsFlow()

    // SharedFlow for user intents / actions with unlimited buffer
    private val _actionMutableFlow = MutableSharedFlow<Action>(extraBufferCapacity = Int.MAX_VALUE)

    // Public method to process an action/intent from UI
    fun processIntent(action: Action) {
        check(_actionMutableFlow.tryEmit(action)) { "Failed to emit action: $action" }
    }

    // Helper for ViewModel to send one-time events to UI
    protected fun sendEvent(event: Event) {
        eventChannel.trySend(event)
    }

    // Helper to update UI state
    fun setState(newState: State) {
        _viewState.value = newState
    }

    // Abstract: clear any ViewModel state if needed
    abstract fun clearState()

    // Abstract: handle actions when emitted
    abstract fun onActionTrigger(action: Action?)

    init {
        viewModelScope.launch {
            _actionMutableFlow.collect { action ->
                onActionTrigger(action)
            }
        }
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        eventChannel.close()
    }
}