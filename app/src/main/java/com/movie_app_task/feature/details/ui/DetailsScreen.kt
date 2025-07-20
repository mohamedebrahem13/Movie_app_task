package com.movie_app_task.feature.details.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.movie_app_task.R
import com.movie_app_task.feature.details.ui.composable.MovieDetailsContent
import com.movie_app_task.feature.details.ui.viewmodel.MovieDetailsContract.*
import com.movie_app_task.feature.details.ui.viewmodel.MovieDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    movieId: Int,
    title: String,
    viewModel: MovieDetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit

) {
    val state by viewModel.viewState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(movieId) {
        viewModel.onActionTrigger(Action.LoadMovieDetails(movieId))
    }
    LaunchedEffect(Unit) {
        viewModel.singleEvent.collect { event ->
            when (event) {
                is Event.ShowError -> {
                    Toast.makeText(context, event.error.message, Toast.LENGTH_SHORT).show()
                }

                Event.NavigateBack -> onBackClick()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        TopAppBar(
            title = { Text(title) },
            navigationIcon = {
                IconButton(onClick = {
                    viewModel.onActionTrigger(Action.OnBackClicked)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        )

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
                    Text(text = "Error: ${currentState.error.message ?: stringResource(R.string.something_went_wrong)}")
                }
            }
        }
    }
}