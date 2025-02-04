package com.pperotti.android.moviescatalogapp.data.movie

import javax.inject.Inject
import javax.inject.Singleton

interface MovieRemoteDataSource {

    /**
     * Retrieve the list of movies from the network
     *
     * @param includeAdult Determine whether it includes adult files or not
     * @param includeVideo Determine whether it includes information about an associated video.
     */
    suspend fun fetchMovieList(includeAdult: Boolean = false, includeVideo: Boolean = false): MovieListResult
}

@Singleton
class DefaultMovieRemoteDataSource @Inject constructor(
    val tmdbApi: TmdbApi
) : MovieRemoteDataSource {

    override suspend fun fetchMovieList(includeAdult: Boolean, includeVideo: Boolean): MovieListResult {
        return tmdbApi.fetchMovieList(includeAdult, includeVideo)
    }
}