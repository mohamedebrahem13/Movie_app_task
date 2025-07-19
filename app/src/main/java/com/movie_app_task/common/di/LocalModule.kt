package com.movie_app_task.common.di

import android.content.Context
import androidx.room.Room
import com.movie_app_task.common.Constants.MOVIE_DATABASE_NAME
import com.movie_app_task.common.data.repository.local.db.MovieDao
import com.movie_app_task.common.data.repository.local.db.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(
        @ApplicationContext appContext: Context
    ): MovieDatabase {
        return Room.databaseBuilder(
            appContext,
            MovieDatabase::class.java,
            MOVIE_DATABASE_NAME
        ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(database: MovieDatabase): MovieDao = database.movieDao()

}