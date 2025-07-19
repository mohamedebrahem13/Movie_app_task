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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetPopularMoviesLocalUseCaseTest {

    private lateinit var repository: MoviesRepository
    private lateinit var useCase: GetPopularMoviesLocalUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetPopularMoviesLocalUseCase(repository)
    }

    @Test
    fun `invoke returns success when repository returns movies`() = runTest {
        val movies = listOf(
            Movie(1, "Movie 1", "/path1.jpg", "2025-01-01", 7.5),
            Movie(2, "Movie 2", "/path2.jpg", "2025-02-01", 8.2)
        )
        coEvery { repository.getPopularMoviesLocal() } returns flowOf(movies)

        useCase().test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            assertThat((result as Resource.Success).model).isEqualTo(movies)
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns failure when repository throws unknown exception`() = runTest {
        val error = RuntimeException("Something went wrong")
        coEvery { repository.getPopularMoviesLocal() } throws error

        useCase().test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Failure::class.java)
            val failure = result as Resource.Failure
            assertThat(failure.exception).isInstanceOf(UnknownException::class.java)
            assertThat(failure.exception.message).isEqualTo("Something went wrong")
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns success with empty list`() = runTest {
        val movies = emptyList<Movie>()
        coEvery { repository.getPopularMoviesLocal() } returns flowOf(movies)

        useCase().test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            assertThat((result as Resource.Success).model).isEmpty()
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns failure when MovieDomainException is thrown`() = runTest {
        val domainError = object : MovieDomainException("Domain failure") {}
        coEvery { repository.getPopularMoviesLocal() } throws domainError

        useCase().test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Failure::class.java)
            val failure = result as Resource.Failure
            assertThat(failure.exception).isEqualTo(domainError)
            awaitComplete()
        }
    }
}