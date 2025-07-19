package com.movie_app_task.common.ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IBaseViewModel<Action : ViewAction, Event : ViewEvent, State : ViewState> {

    val singleEvent: Flow<Event>

    val viewState: StateFlow<State>

    /**
     * Must be called on [kotlinx.coroutines.Dispatchers.Main], otherwise throws exception.
     *
     * If you want to process an intent from other CoroutineDispatcher,
     * use `withContext(Dispatchers.Main.immediate) { processIntent(action) }`.
     */
    fun processIntent(action: Action)
}