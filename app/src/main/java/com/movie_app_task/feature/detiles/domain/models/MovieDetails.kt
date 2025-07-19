package com.movie_app_task.feature.detiles.domain.models

data class MovieDetails(
    val id: Int,
    val title: String,
    val posterPath: String,
    val releaseDate: String,
    val voteAverage: Double,
    val adult: Boolean,
    val backdropPath: String,
    val genreIds: List<Int>,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val video: Boolean,
    val voteCount: Int
)