package com.pperotti.android.moviescatalogapp.data.movie

import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {

    @GET("discover/movie?language=en-US&page=1&sort_by=popularity")
    suspend fun fetchMovieList(
        @Query("include_adult") adult: Boolean,
        @Query("include_video") video: Boolean
    ): ListResult
}