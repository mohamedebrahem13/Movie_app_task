package com.movie_app_task.feature.movie_list.data.mapper

import com.movie_app_task.common.Constants.BASE_IMAGE_URL
import com.movie_app_task.feature.movie_list.data.models.dto.MovieDto
import com.movie_app_task.feature.movie_list.data.models.dto.MoviesResponseDto
import com.movie_app_task.feature.movie_list.data.models.entity.MovieEntity
import com.movie_app_task.feature.movie_list.domain.models.Movie

object MoviesMapper {

    fun dtoToDomain(model: MoviesResponseDto): List<Movie> {
        return model.results?.map { dto ->
            dtoToDomain(dto)
        } ?: emptyList()
    }

    private fun dtoToDomain(dto: MovieDto): Movie {
        return Movie(
            id = dto.id ?: 0,
            title = dto.title.orEmpty(),
            posterPath = BASE_IMAGE_URL + dto.posterPath.orEmpty(),
            releaseDate = dto.releaseDate.orEmpty(),
            voteAverage = dto.voteAverage ?: 0.0
        )
    }

    fun entityToDomain(entities: List<MovieEntity>): List<Movie> {
        return entities.map { entity ->
            Movie(
                id = entity.id,
                title = entity.title,
                posterPath = entity.posterPath,
                releaseDate = entity.releaseDate,
                voteAverage = entity.voteAverage
            )
        }
    }

    fun dtoToEntity(dto: MovieDto): MovieEntity {
        return MovieEntity(
            id = dto.id ?: 0,
            title = dto.title.orEmpty(),
            posterPath = BASE_IMAGE_URL + dto.posterPath.orEmpty(),
            releaseDate = dto.releaseDate.orEmpty(),
            voteAverage = dto.voteAverage ?: 0.0,
            adult = dto.adult ?: false,
            backdropPath = BASE_IMAGE_URL + dto.backdropPath.orEmpty(),
            genreIds = dto.genreIds.orEmpty(),
            originalLanguage = dto.originalLanguage.orEmpty(),
            originalTitle = dto.originalTitle.orEmpty(),
            overview = dto.overview.orEmpty(),
            popularity = dto.popularity ?: 0.0,
            video = dto.video ?: false,
            voteCount = dto.voteCount ?: 0
        )
    }

    fun dtoListToEntityList(dtoList: List<MovieDto>): List<MovieEntity> {
        return dtoList.map { dtoToEntity(it) }
    }
}