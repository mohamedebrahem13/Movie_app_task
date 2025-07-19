package com.movie_app_task.feature.movie_list.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.movie_app_task.common.domain.utils.MovieDomainException
import com.movie_app_task.common.domain.utils.Resource
import com.movie_app_task.common.domain.utils.UnknownException
import com.movie_app_task.feature.movie_list.domain.models.Movie
import com.movie_app_task.feature.movie_list.domain.repository.MoviesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchMoviesByNameRemoteUseCaseTest {

    private lateinit var repository: MoviesRepository
    private lateinit var useCase: SearchMoviesByNameRemoteUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = SearchMoviesByNameRemoteUseCase(repository)
    }

    @Test
    fun `invoke returns success when repository returns movies`() = runTest {
        // Given
        val query = "Avengers"
        val page = 1
        val movies = listOf(
            Movie(1, "Avengers: Endgame", "/poster1.jpg", "2019-04-26", 8.4),
            Movie(2, "Avengers: Infinity War", "/poster2.jpg", "2018-04-27", 8.5)
        )
        coEvery { repository.searchMoviesByNameRemote(query, page) } returns movies

        // When
        useCase(query, page).test {
            val result = awaitItem()

            // Then
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            assertThat((result as Resource.Success).model).isEqualTo(movies)

            awaitComplete()
        }
    }

    @Test
    fun `invoke returns failure when repository throws MovieDomainException`() = runTest {
        // Given
        val query = "Avengers"
        val page = 1
        val domainException = object : MovieDomainException("Movie not found") {}
        coEvery { repository.searchMoviesByNameRemote(query, page) } throws domainException

        // When
        useCase(query, page).test {
            val result = awaitItem()

            // Then
            assertThat(result).isInstanceOf(Resource.Failure::class.java)
            val failure = result as Resource.Failure
            assertThat(failure.exception).isEqualTo(domainException)

            awaitComplete()
        }
    }

    @Test
    fun `invoke returns failure when repository throws unknown exception`() = runTest {
        // Given
        val query = "Avengers"
        val page = 1
        val exception = RuntimeException("Timeout")
        coEvery { repository.searchMoviesByNameRemote(query, page) } throws exception

        // When
        useCase(query, page).test {
            val result = awaitItem()

            // Then
            assertThat(result).isInstanceOf(Resource.Failure::class.java)
            val failure = result as Resource.Failure
            assertThat(failure.exception).isInstanceOf(UnknownException::class.java)
            assertThat(failure.exception.message).isEqualTo("Timeout")

            awaitComplete()
        }
    }
}