package com.pperotti.android.moviescatalogapp.data.movie

import javax.inject.Inject
import javax.inject.Singleton

interface MovieLocalDataSource {
    /**
     * Retrieve the list of stored movies
     */
    suspend fun getMovieListResult(): DataMovieListResult

    /**
     * Persist the MovieListResults
     */
    suspend fun saveMovieListResult(remoteMovieListResult: RemoteMovieListResult)

    /**
     * Determine whether there is a previous stored record in the DB
     */
    suspend fun hasMovieListResult(): Boolean
}

@Singleton
class DefaultMovieLocalDataSource @Inject constructor(
    val movieDao: MovieDao
) : MovieLocalDataSource {

    override suspend fun getMovieListResult(): DataMovieListResult {
        val storageMovieListResult = movieDao.getMovieListResult()
        val movies = movieDao.getAllMovies()
        return storageMovieListResult.toMovieListResult(movies)
    }

    override suspend fun saveMovieListResult(remoteMovieListResult: RemoteMovieListResult) {
        movieDao.deleteMovieListResult()
        movieDao.deleteAllMovies()
        movieDao.insertMovieListResult(remoteMovieListResult.toStorageMovieListResult())
        movieDao.insertAll(remoteMovieListResult.results.map { it.toStorageMovieItem(remoteMovieListResult.page) })
    }

    override suspend fun hasMovieListResult(): Boolean {
        return movieDao.hasMovieListResult() > 0
    }

}
