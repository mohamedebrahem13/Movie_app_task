package com.movie_app_task.common.data.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

suspend fun <T> safeCall(block: suspend () -> T): T {
    return try {
        block()
    } catch (e: Exception) {
        throw mapToMovieDataException(e)
    }
}

fun <T> safeFlow(block: () -> Flow<T>): Flow<T> {
    return flow {
        emitAll(block())
    }.catch { e ->
        throw mapToMovieDataException(e)
    }
}

private fun mapToMovieDataException(throwable: Throwable): MovieDataException = when (throwable) {
    is android.database.sqlite.SQLiteException,
    is java.sql.SQLException,
    is android.database.CursorIndexOutOfBoundsException,
    is android.database.StaleDataException -> DatabaseCorruptionException(throwable.message)

    is java.io.IOException -> IOErrorException(throwable.message)

    is IllegalArgumentException,
    is IllegalStateException -> IllegalArgumentExceptionCustom(throwable.message)

    is NullPointerException -> DatabaseCorruptionException(throwable.message)

    else -> UnknownException(throwable.message)
}