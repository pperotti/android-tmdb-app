package com.pperotti.android.moviescatalogapp.data.di

/**
 * This file contains all the Hilt Modules require to handle dependencies
 * on the Data Layer
 */
import android.content.Context
import androidx.room.Room
import com.pperotti.android.moviescatalogapp.BuildConfig
import com.pperotti.android.moviescatalogapp.data.TmdbService
import com.pperotti.android.moviescatalogapp.data.movie.DefaultMovieLocalDataSource
import com.pperotti.android.moviescatalogapp.data.movie.DefaultMovieRemoteDataSource
import com.pperotti.android.moviescatalogapp.data.movie.DefaultMovieRepository
import com.pperotti.android.moviescatalogapp.data.movie.MovieDao
import com.pperotti.android.moviescatalogapp.data.movie.MovieDatabase
import com.pperotti.android.moviescatalogapp.data.movie.MovieLocalDataSource
import com.pperotti.android.moviescatalogapp.data.movie.MovieRemoteDataSource
import com.pperotti.android.moviescatalogapp.data.movie.MovieRepository
import com.pperotti.android.moviescatalogapp.data.movie.TmdbApi
import com.pperotti.android.moviescatalogapp.di.IoDispatcher

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private const val BASE_URL = BuildConfig.API_BASE_URL
    private const val TOKEN = BuildConfig.SERVICE_TOKEN

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideTmdbService(retrofit: Retrofit): TmdbApi {
        return retrofit.create(TmdbService::class.java)
    }

    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    fun provideAuthInterceptor(): AuthInterceptor {
        return AuthInterceptor(TOKEN)
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Log request/response details
        }
    }
}

// Authentication Interceptor
class AuthInterceptor(val token: String) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()


        // Add authentication headers
        val authenticatedRequest: Request = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movies_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(database: MovieDatabase): MovieDao {
        return database.movieDao()
    }

    @Provides
    @Singleton
    fun provideMovieLocalDataSource(movieDao: MovieDao): MovieLocalDataSource {
        return DefaultMovieLocalDataSource(movieDao)
    }

    @Provides
    @Singleton
    fun provideMovieRemoteDataSource(tmdbApi: TmdbApi): MovieRemoteDataSource {
        return DefaultMovieRemoteDataSource(tmdbApi)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        localDataSource: MovieLocalDataSource,
        remoteDataSource: MovieRemoteDataSource,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): MovieRepository {
        return DefaultMovieRepository(
            localDataSource,
            remoteDataSource,
            dispatcher
        )
    }
}
