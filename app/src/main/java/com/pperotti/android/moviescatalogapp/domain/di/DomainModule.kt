package com.pperotti.android.moviescatalogapp.domain.di

import com.pperotti.android.moviescatalogapp.data.movie.MovieRepository
import com.pperotti.android.moviescatalogapp.domain.usecase.DefaultGetLatestMovies
import com.pperotti.android.moviescatalogapp.domain.usecase.GetLatestMovies
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Provides
    @Singleton
    fun provideGetLatestMovies(movieRepository: MovieRepository): GetLatestMovies {
        return DefaultGetLatestMovies(movieRepository)
    }

}