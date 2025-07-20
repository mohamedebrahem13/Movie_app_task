package com.movie_app_task

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.movie_app_task.feature.details.ui.DetailsScreen
import com.movie_app_task.feature.movie_list.ui.screen.home.HomeScreen
import com.movie_app_task.android.theme.MovieAppTaskTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieAppTaskTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavHost()
                }
            }
        }
    }
}
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Destination.Home) {
        composable<Destination.Home> {
            HomeScreen(onMovieClick = { movieId,title ->
                navController.navigate(Destination.Details(movieId, title))
            })
        }
        composable<Destination.Details> { backStackEntry ->
            val details = backStackEntry.toRoute<Destination.Details>()
            DetailsScreen(
                movieId = details.movieId,
                title = details.movieTitle,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
@Serializable
sealed class Destination {

    @Serializable
    data object Home : Destination()

    @Serializable
    data class Details(val movieId: Int, val movieTitle: String) : Destination()
}