package com.movie_app_task.feature.movie_list.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.movie_app_task.common.domain.utils.Resource
import com.movie_app_task.feature.movie_list.domain.models.Movie
import com.movie_app_task.feature.movie_list.domain.repository.MoviesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchMoviesByNameUseCaseTest {

    private lateinit var repository: MoviesRepository
    private lateinit var useCase: SearchMoviesByNameUseCase

    private val fakeMovies = listOf(
        Movie(id = 1, title = "Inception", posterPath = "path1", releaseDate = "2010", voteAverage = 8.8),
        Movie(id = 2, title = "Interstellar", posterPath = "path2", releaseDate = "2014", voteAverage = 8.6)
    )

    @Before
    fun setup() {
        repository = mockk()
        useCase = SearchMoviesByNameUseCase(repository)
    }

    @Test
    fun `invoke returns success when repository emits movies`() = runTest {
        // Given
        val query = "In"
        coEvery { repository.searchMoviesByName(query) } returns flowOf(fakeMovies)

        // When + Then
        useCase(query).test {
            val item = awaitItem()
            assertThat(item).isInstanceOf(Resource.Success::class.java)
            assertThat((item as Resource.Success).model).isEqualTo(fakeMovies)
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns failure when repository throws exception`() = runTest {
        // Given
        val query = "In"
        coEvery { repository.searchMoviesByName(query) } throws RuntimeException("Something went wrong")

        // When + Then
        useCase(query).test {
            val item = awaitItem()
            assertThat(item).isInstanceOf(Resource.Failure::class.java)
            assertThat((item as Resource.Failure).exception.message).isEqualTo("Something went wrong")
            awaitComplete()
        }
    }
}