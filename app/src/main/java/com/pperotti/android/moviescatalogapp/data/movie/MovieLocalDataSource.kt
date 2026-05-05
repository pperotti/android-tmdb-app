package com.pperotti.android.moviescatalogapp.data.movie

import javax.inject.Inject
import javax.inject.Singleton

interface MovieLocalDataSource {
    /**
     * Retrieve the list of stored movies
     */
    suspend fun getMovieListResult(page: Int): DataMovieListResult

    /**
     * Persist the MovieListResults
     */
    suspend fun saveMovieListResult(remoteMovieListResult: RemoteMovieListResult)

    /**
     * Determine whether there is a previous stored record in the DB
     */
    suspend fun hasMovieListResult(page: Int): Boolean
}

@Singleton
class DefaultMovieLocalDataSource
    @Inject
    constructor(
        val movieDao: MovieDao,
    ) : MovieLocalDataSource {
        override suspend fun getMovieListResult(page: Int): DataMovieListResult {
            val storageMovieListResult = movieDao.getMovieListResult(page)
            val movies = movieDao.getMoviesByPage(page)
            return storageMovieListResult.toMovieListResult(movies)
        }

        override suspend fun saveMovieListResult(remoteMovieListResult: RemoteMovieListResult) {
            if (remoteMovieListResult.page == 1) {
                movieDao.deleteMovieListResult()
                movieDao.deleteAllMovies()
            }
            movieDao.insertMovieListResult(remoteMovieListResult.toStorageMovieListResult())
            movieDao.insertAll(
                remoteMovieListResult.results.map {
                    it.toStorageMovieItem(remoteMovieListResult.page)
                },
            )
        }

        override suspend fun hasMovieListResult(page: Int): Boolean {
            return movieDao.hasMovieListResult(page) > 0
        }
    }
