package com.movie_app_task.feature.details.data.mapper

import com.movie_app_task.feature.details.domain.models.MovieDetails
import com.movie_app_task.feature.movie_list.data.models.entity.MovieEntity

object MovieDetailsMapper {

    fun entityToDomain(entity: MovieEntity): MovieDetails {
        return MovieDetails(
            id = entity.id,
            adult = entity.adult,
            backdropPath = entity.backdropPath,
            genreIds = entity.genreIds,
            originalLanguage = entity.originalLanguage,
            originalTitle = entity.originalTitle,
            overview = entity.overview,
            popularity = entity.popularity,
            title = entity.title,
            posterPath = entity.posterPath,
            releaseDate = entity.releaseDate,
            video = entity.video,
            voteAverage = entity.voteAverage,
            voteCount = entity.voteCount
        )
    }
}