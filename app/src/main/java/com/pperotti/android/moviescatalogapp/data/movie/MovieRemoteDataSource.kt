package com.pperotti.android.moviescatalogapp.data.movie

import javax.inject.Inject
import javax.inject.Singleton

interface MovieRemoteDataSource {

    /**
     * Retrieve the list of movies from the network
     */
    suspend fun fetchMovieList(includeAdult: Boolean = false, includeVideo: Boolean = false): ListResult
}

@Singleton
class DefaultMovieRemoteDataSource @Inject constructor(
    val tmdbApi: TmdbApi
) : MovieRemoteDataSource {

    override suspend fun fetchMovieList(includeAdult: Boolean, includeVideo: Boolean): ListResult {
        return tmdbApi.fetchMovieList(includeAdult, includeVideo)
    }
}