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


class GetPopularMoviesRemoteUseCaseTest {

    private lateinit var repository: MoviesRepository
    private lateinit var useCase: GetPopularMoviesRemoteUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetPopularMoviesRemoteUseCase(repository)
    }

    @Test
    fun `invoke returns success when repository returns movies`() = runTest {
        // Given
        val movies = listOf(
            Movie(1, "Movie 1", "/poster1.jpg", "2025-01-01", 8.0),
            Movie(2, "Movie 2", "/poster2.jpg", "2025-02-01", 7.5)
        )
        coEvery { repository.getPopularMoviesRemote() } returns movies

        // When
        useCase().test {
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
        val domainException = object : MovieDomainException("API limit exceeded") {}
        coEvery { repository.getPopularMoviesRemote() } throws domainException

        // When
        useCase().test {
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
        val exception = RuntimeException("Server error")
        coEvery { repository.getPopularMoviesRemote() } throws exception

        // When
        useCase().test {
            val result = awaitItem()

            // Then
            assertThat(result).isInstanceOf(Resource.Failure::class.java)
            val failure = result as Resource.Failure
            assertThat(failure.exception).isInstanceOf(UnknownException::class.java)
            assertThat(failure.exception.message).isEqualTo("Server error")

            awaitComplete()
        }
    }
}