package com.movie_app_task.feature.movie_list.domain.usecase

import com.movie_app_task.common.domain.utils.InvalidSearchQueryException

class ValidateSearchQueryUseCase {

    operator fun invoke(query: String): String {
        val trimmed = query.trim()

        if (trimmed.isEmpty()) {
            throw InvalidSearchQueryException("Search query must not be empty.")
        }

        val invalidDotPattern = Regex("""^\.|\.{2,}""")
        val allowedPattern = Regex("^[a-zA-Z0-9 ]+$")

        if (invalidDotPattern.containsMatchIn(trimmed) || !allowedPattern.matches(trimmed)) {
            throw InvalidSearchQueryException("Search query is invalid. Please use only letters, numbers, and spaces.")
        }

        return trimmed
    }
}