package com.movie_app_task.common.di

import com.movie_app_task.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.movie_app_task.common.Constants
import com.movie_app_task.common.data.repository.remote.adapter_factory.MovieAppCallAdapterFactory
import com.movie_app_task.common.data.repository.remote.MoviesApiService
import com.movie_app_task.common.data.repository.remote.RetrofitNetworkProvider
import com.movie_app_task.common.data.repository.remote.converter.ExceptionConverter
import com.movie_app_task.common.data.repository.remote.converter.IExceptionConverter
import com.movie_app_task.common.domain.remote.INetworkProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Named
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("base_url")
    fun provideBaseUrl(): String = Constants.BASE_URL

    @Provides
    @Singleton
    @Named("access_token")
    fun provideAccessToken(): String = BuildConfig.ACCESS_TOKEN

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkHttpClient(@Named("access_token") accessToken: String): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val newUrl = originalRequest.url.newBuilder()
                .addQueryParameter("include_adult", "false")
                .addQueryParameter("include_video", "false")
                .addQueryParameter("language", "en-US")
                .addQueryParameter("sort_by", "popularity.desc")
                .build()

            val newRequest = originalRequest.newBuilder()
                .url(newUrl)
                .addHeader("Authorization", "Bearer $accessToken")
                .build()

            chain.proceed(newRequest)
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideExceptionConverter(): IExceptionConverter {
        return ExceptionConverter()
    }

    @Provides
    @Singleton
    fun provideCallAdapter(exceptionConverter: IExceptionConverter): MovieAppCallAdapterFactory {
        return MovieAppCallAdapterFactory.create(exceptionConverter)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        gson: Gson,
        @Named("base_url") baseUrl: String,
        client: OkHttpClient,
        movieAppCallAdapterFactory: MovieAppCallAdapterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addCallAdapterFactory(movieAppCallAdapterFactory)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    fun provideRetrofitNetwork(apiService: MoviesApiService): INetworkProvider {
        return RetrofitNetworkProvider(apiService)
    }
    @Provides
    @Singleton
    fun providesMoviesApiService(retrofit: Retrofit): MoviesApiService =
        retrofit.create(MoviesApiService::class.java)
}