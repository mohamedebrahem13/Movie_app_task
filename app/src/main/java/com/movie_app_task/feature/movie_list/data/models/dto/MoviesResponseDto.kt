package com.movie_app_task.feature.movie_list.data.models.dto

import com.google.gson.annotations.SerializedName

data class MoviesResponseDto(
    @SerializedName("page")
    val page: Int? = null,

    @SerializedName("results")
    val results: List<MovieDto>? = null,

    @SerializedName("total_pages")
    val totalPages: Int? = null,

    @SerializedName("total_results")
    val totalResults: Int? = null
)