package com.movie_app_task.common.domain.utils

sealed class Resource<out Model> {
    data class Success<out Model>(val model: Model) : Resource<Model>()
    data class Failure(val exception: MovieDomainException) : Resource<Nothing>()
}