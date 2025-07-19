package com.movie_app_task.feature.movie_list.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.movie_app_task.common.domain.utils.InvalidSearchQueryException
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ValidateSearchQueryUseCaseTest {

    private lateinit var useCase: ValidateSearchQueryUseCase

    @Before
    fun setUp() {
        useCase = ValidateSearchQueryUseCase()
    }

    @Test
    fun `given valid query when validated then returns same query`() = runTest {
        val query = "Avengers"
        val result = useCase(query)
        assertThat(result).isEqualTo(query)
    }

    @Test
    fun `given empty query when validated then throws InvalidSearchQueryException`() = runTest {
        val query = ""
        val exception = runCatching { useCase(query) }.exceptionOrNull()
        assertThat(exception).isInstanceOf(InvalidSearchQueryException::class.java)
        assertThat(exception?.message).isEqualTo("Search query must not be empty.")
    }

    @Test
    fun `given whitespace query when validated then throws InvalidSearchQueryException`() = runTest {
        val query = "     "
        val exception = runCatching { useCase(query) }.exceptionOrNull()
        assertThat(exception).isInstanceOf(InvalidSearchQueryException::class.java)
        assertThat(exception?.message).isEqualTo("Search query must not be empty.")
    }

    @Test
    fun `given query with dash when validated then throws InvalidSearchQueryException`() = runTest {
        val query = "Spider-Man 2024"
        val exception = runCatching { useCase(query) }.exceptionOrNull()
        assertThat(exception).isInstanceOf(InvalidSearchQueryException::class.java)
        assertThat(exception?.message).isEqualTo("Search query is invalid. Please use only letters, numbers, and spaces.")
    }

    @Test
    fun `given valid alphanumeric query when validated then returns same query`() = runTest {
        val query = "IronMan123"
        val result = useCase(query)
        assertThat(result).isEqualTo(query)
    }

    @Test
    fun `given query starting with dot when validated then throws InvalidSearchQueryException`() = runTest {
        val query = ".Marvel"
        val exception = runCatching { useCase(query) }.exceptionOrNull()
        assertThat(exception).isInstanceOf(InvalidSearchQueryException::class.java)
        assertThat(exception?.message).isEqualTo("Search query is invalid. Please use only letters, numbers, and spaces.")
    }

    @Test
    fun `given query with repeated dots when validated then throws InvalidSearchQueryException`() = runTest {
        val query = "Marvel..Studios"
        val exception = runCatching { useCase(query) }.exceptionOrNull()
        assertThat(exception).isInstanceOf(InvalidSearchQueryException::class.java)
        assertThat(exception?.message).isEqualTo("Search query is invalid. Please use only letters, numbers, and spaces.")
    }

    @Test
    fun `given query with single dot in middle when validated then throws InvalidSearchQueryException`() = runTest {
        val query = "Marvel.Studios"
        val exception = runCatching { useCase(query) }.exceptionOrNull()
        assertThat(exception).isInstanceOf(InvalidSearchQueryException::class.java)
        assertThat(exception?.message).isEqualTo("Search query is invalid. Please use only letters, numbers, and spaces.")
    }

    @Test
    fun `given query with symbols like at sign when validated then throws InvalidSearchQueryException`() = runTest {
        val query = "Movie@2024"
        val exception = runCatching { useCase(query) }.exceptionOrNull()
        assertThat(exception).isInstanceOf(InvalidSearchQueryException::class.java)
        assertThat(exception?.message).isEqualTo("Search query is invalid. Please use only letters, numbers, and spaces.")
    }
}