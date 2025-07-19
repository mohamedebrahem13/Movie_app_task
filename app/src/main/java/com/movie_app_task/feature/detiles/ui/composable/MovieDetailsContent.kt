package com.movie_app_task.feature.detiles.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.movie_app_task.R
import com.movie_app_task.feature.detiles.domain.models.MovieDetails
import com.movie_app_task.feature.movie_list.ui.screen.home.composable.Rating

@Composable
fun MovieDetailsContent(
    movie: MovieDetails,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f)
        ) {
            AsyncImage(
                model = movie.posterPath,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.place_holder),
                error = painterResource(id = R.drawable.place_holder),
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
            )

            Rating(
                value = movie.voteAverage,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            )
        }

        Text(
            text = stringResource(R.string.rating_format, movie.voteAverage, movie.voteCount),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )

        Text(
            text = stringResource(R.string.release_date_format, movie.releaseDate),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Text(
            text = stringResource(R.string.original_title_format, movie.originalTitle),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Text(
            text = stringResource(R.string.language_format, movie.originalLanguage.uppercase()),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Text(
            text = stringResource(R.string.popularity_format, movie.popularity),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Text(
            text = if (movie.adult) stringResource(R.string._18_adult_content) else stringResource(R.string.all_ages),
            style = MaterialTheme.typography.bodyMedium,
            color = if (movie.adult) Color.Red else Color.Green,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = if (movie.video) stringResource(R.string.video_available) else stringResource(R.string.no_video),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Text(
            text = movie.overview,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 10,
            overflow = TextOverflow.Ellipsis
        )
    }
}