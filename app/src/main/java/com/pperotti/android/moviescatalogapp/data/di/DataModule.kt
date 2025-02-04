package com.pperotti.android.moviescatalogapp.data.di

/**
 * This file contains all the Hilt Modules require to handle dependencies
 * on the Data Layer
 */

import com.pperotti.android.moviescatalogapp.data.TmdbService
import com.pperotti.android.moviescatalogapp.data.movie.DefaultMovieRemoteDataSource
import com.pperotti.android.moviescatalogapp.data.movie.DefaultMovieRepository
import com.pperotti.android.moviescatalogapp.data.movie.MovieRemoteDataSource
import com.pperotti.android.moviescatalogapp.data.movie.MovieRepository
import com.pperotti.android.moviescatalogapp.data.movie.TmdbApi
import com.pperotti.android.moviescatalogapp.di.IoDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    private const val BASE_URL = "https://api.themoviedb.org/3/"

    // WARNING: This value should not be hardcoded due to security concerns. This value should be
    // received via a config service like remote config or similar
    private const val TOKEN =
        "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NDUxY2VjNmM5MTZiZmYzOWM2YTQwNTZhY2E4MDA4OSIsIm5iZiI6MTU0NzA0MjI4OC40NDQsInN1YiI6IjVjMzVmZGYwYzNhMzY4MjcyNDFiZWIyZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.1Ve-Aft6IcP7ng9GgvzCykTVzbpHXltbAI9M2o1yXso"

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

//    @Provides
//    @Singleton
//    fun provideBreedDatabase(@ApplicationContext context: Context): BreedDatabase {
//        return Room.databaseBuilder(
//            context,
//            BreedDatabase::class.java,
//            "breed_database"
//        ).build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideBreedDao(database: BreedDatabase): BreedDao {
//        return database.breedDao()
//    }
//
//    @Provides
//    @Singleton
//    fun provideBreedLocalDataSource(breedDao: BreedDao): BreedLocalDataSource {
//        return DefaultBreedLocalDataSource(breedDao)
//    }

    @Provides
    @Singleton
    fun provideMovieRemoteDataSource(tmdbApi: TmdbApi): MovieRemoteDataSource {
        return DefaultMovieRemoteDataSource(tmdbApi)
    }

    @Provides
    @Singleton
    fun provideBreedRepository(
        //localDataSource: BreedLocalDataSource,
        remoteDataSource: MovieRemoteDataSource,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): MovieRepository {
        return DefaultMovieRepository(remoteDataSource, dispatcher)
    }

}
