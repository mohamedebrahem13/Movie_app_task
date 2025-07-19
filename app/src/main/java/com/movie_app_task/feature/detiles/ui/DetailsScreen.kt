package com.movie_app_task.feature.detiles.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.movie_app_task.feature.detiles.ui.composable.MovieDetailsContent
import com.movie_app_task.feature.detiles.ui.viewmodel.MovieDetailsContract.*
import com.movie_app_task.feature.detiles.ui.viewmodel.MovieDetailsViewModel

@Composable
fun DetailsScreen(
    movieId: Int,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.viewState.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.onActionTrigger(Action.LoadMovieDetails(movieId))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        when (val currentState = state) {
            is State.Idle, is State.Loading -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            }

            is State.Success -> {
                MovieDetailsContent(movie = currentState.movie)
            }

            is State.Failure -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(text = "Error: ${currentState.error.message ?: "Something went wrong"}")
                }
            }
        }
    }
}