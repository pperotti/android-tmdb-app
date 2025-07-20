package com.pperotti.android.moviescatalogapp.data.movie

import javax.inject.Inject
import javax.inject.Singleton

interface MovieRemoteDataSource {
    /**
     * Retrieve the list of movies from the network.
     *
     * @param includeAdult Determine whether it includes adult files or not
     * @param includeVideo Determine whether it includes information about an associated video.
     * @param page The value used by TMDB to determine the list of movies it sends.
     *
     * @return One page of movies encapsulated within the RemoteMovieListResult
     */
    suspend fun fetchMovieList(
        includeAdult: Boolean = false,
        includeVideo: Boolean = false,
        page: Int = 1,
    ): RemoteMovieListResult

    /**
     * Retrieve the full details for a movie given an id.
     *
     * @param id The ID for the selected movie.
     * @return All the details TMDB has associated with this movie
     *         encapsulated in RemoteMovieDetails class
     */
    suspend fun fetchMovieDetails(id: Int): RemoteMovieDetails
}

@Singleton
class DefaultMovieRemoteDataSource
    @Inject
    constructor(
        val tmdbApi: TmdbApi,
    ) : MovieRemoteDataSource {
        override suspend fun fetchMovieList(
            includeAdult: Boolean,
            includeVideo: Boolean,
            page: Int,
        ): RemoteMovieListResult {
            return tmdbApi.fetchMovieList(includeAdult, includeVideo, page)
        }

        override suspend fun fetchMovieDetails(id: Int): RemoteMovieDetails {
            return tmdbApi.fetchMovieDetails(id)
        }
    }
