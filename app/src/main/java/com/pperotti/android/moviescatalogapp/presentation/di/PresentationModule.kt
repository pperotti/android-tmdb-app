package com.pperotti.android.moviescatalogapp.presentation.di

import com.pperotti.android.moviescatalogapp.domain.usecase.GetLatestMovies
import com.pperotti.android.moviescatalogapp.domain.usecase.GetMovieDetails
import com.pperotti.android.moviescatalogapp.presentation.details.DetailsViewModel
import com.pperotti.android.moviescatalogapp.presentation.main.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * This file contains the Hilt module definition for all dependencies
 * used from presentation layer.
 */
@Module
@InstallIn(SingletonComponent::class)
class PresentationModule {

    @Provides
    fun provideMainViewModel(getLatestMovies: GetLatestMovies): MainViewModel {
        return MainViewModel(getLatestMovies)
    }

    @Provides
    fun provideDetailsViewModel(getMovieDetails: GetMovieDetails): DetailsViewModel {
        return DetailsViewModel(getMovieDetails)
    }
}
