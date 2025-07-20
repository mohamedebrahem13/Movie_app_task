package com.movie_app_task.feature.movie_list.ui.screen.home

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.movie_app_task.R
import com.movie_app_task.feature.movie_list.domain.models.Movie
import com.movie_app_task.feature.movie_list.ui.screen.home.composable.HomeHeader
import com.movie_app_task.feature.movie_list.ui.screen.home.composable.PosterItemVertically
import com.movie_app_task.feature.movie_list.ui.screen.home.viewmodel.MovieContract
import com.movie_app_task.feature.movie_list.ui.screen.home.viewmodel.MovieViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    viewModel: MovieViewModel = hiltViewModel(),
    onMovieClick: (Int,String) -> Unit
) {
    val context = LocalContext.current

    val state by viewModel.viewState.collectAsState()
    val searchText = state.searchQuery

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.processIntent(MovieContract.MovieAction.OnPermissionResult(granted))
        val message = if (granted) {
            context.getString(R.string.voice_permission_granted)
        } else {
            context.getString(R.string.voice_permission_denied)
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    val voiceSearchHelper = remember {
        VoiceSearchHelper(
            context = context,
            onResult = { result ->
                if (result.isNotBlank()) {
                    viewModel.processIntent(MovieContract.MovieAction.OnSearchQueryChange(result))
                    Toast.makeText(context, "You said: $result", Toast.LENGTH_SHORT).show()
                }
                viewModel.processIntent(MovieContract.MovieAction.OnVoiceSearchFinished)
            },
            onError = { error ->
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                viewModel.processIntent(MovieContract.MovieAction.OnVoiceSearchFinished)
            }
        )
    }

    LaunchedEffect(state.isVoiceRecording, state.isPermissionGranted) {
        if (state.isVoiceRecording && !state.isPermissionGranted) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        } else if (state.isVoiceRecording) {
            voiceSearchHelper.startListening()
        }
    }

    DisposableEffect(Unit) {
        onDispose { voiceSearchHelper.destroy() }
    }

    LaunchedEffect(viewModel.singleEvent) {
        viewModel.singleEvent.collectLatest { event ->
            when (event) {
                is MovieContract.MovieEvent.Navigate -> {
                    onMovieClick(event.movieId, event.movieTitle)
                }

                is MovieContract.MovieEvent.ShowError -> {
                    Toast.makeText(
                        context,
                        event.error.message ?: context.getString(R.string.unknown_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    HomeScreenContent(
        state = state,
        searchText = state.searchQuery,
        onSearchTextChange = { newText ->
            viewModel.processIntent(MovieContract.MovieAction.OnSearchQueryChange(newText))
        },
        onSearchClick = {
            if (searchText.isNotBlank()) {
                viewModel.processIntent(MovieContract.MovieAction.OnSearchQueryChange(state.searchQuery))
            }
        },
        onVoiceClick = {
            viewModel.processIntent(MovieContract.MovieAction.OnVoiceSearchClick)
        },
        onMovieClick = { movieId,movieTitle ->
            viewModel.processIntent(MovieContract.MovieAction.OnMovieClick(movieId,movieTitle))
        }
    )
}
@Composable
fun HomeScreenContent(
    state: MovieContract.MovieState,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchClick: (String) -> Unit,
    onVoiceClick: () -> Unit,
    onMovieClick: (Int,String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        HomeHeader(
            value = searchText,
            onValueChange = onSearchTextChange,
            onSearchClick = onSearchClick,
            endIcon = painterResource(R.drawable.ic_voice),
            onEndIconClick = onVoiceClick
        )

        when {
            state.isLoading -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.error != null -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${state.error.message ?: R.string.unknown_error}"
                    )
                }
            }
            state.movies.isNotEmpty() -> {
                MovieList(movies = state.movies, onMovieClick = onMovieClick)
            }
            else -> Unit

        }
    }
}
@Composable
fun MovieList(
    movies: List<Movie>,
    onMovieClick: (Int,String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(movies) { movie ->
            PosterItemVertically(
                movie = movie,
                modifier = Modifier.fillMaxWidth()
            ) {
                onMovieClick(movie.id, movie.title)
            }
        }
    }
}