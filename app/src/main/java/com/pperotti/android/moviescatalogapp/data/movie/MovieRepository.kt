package com.pperotti.android.moviescatalogapp.data.movie

import com.pperotti.android.moviescatalogapp.data.common.DataResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository used to interact with Data Layer. It should offer its clients the ability to retrieve
 * the listing of movies along with the details of each movie
 */
interface MovieRepository {
    /**
     * Request the repository to retrieve information about movies from
     * the network or local storage.
     *
     * @param forceRefresh Boolean that indicates if the repository should interact with the
     * network source.
     * @return MovieListResult encapsulated inside a RepositoryResponse
     */
    suspend fun fetchMovieList(forceRefresh: Boolean = false): DataResult<DataMovieListResult>

    /**
     * Retrieves from the network the details for the movie with the specified id.
     *
     * @param id The Int value that identifies the movie.
     *
     * @return MovieDetails encapsulated inside a RepositoryResponse
     */
    suspend fun fetchMovieDetails(id: Int): DataResult<DataMovieDetails>
}

// Default implementation
@Singleton
class DefaultMovieRepository
    @Inject
    constructor(
        val localDataSource: MovieLocalDataSource,
        val remoteDataSource: MovieRemoteDataSource,
        val dispatcher: CoroutineDispatcher,
    ) : MovieRepository {
        override suspend fun fetchMovieList(forceRefresh: Boolean): DataResult<DataMovieListResult> {
            return withContext(dispatcher) {
                try {
                    if (forceRefresh || !localDataSource.hasMovieListResult()) {
                        val remoteMovieResultList = remoteDataSource.fetchMovieList()
                        localDataSource.saveMovieListResult(remoteMovieResultList)
                    }
                    DataResult.Success(localDataSource.getMovieListResult())
                } catch (e: IOException) {
                    DataResult.Error(e.localizedMessage, e.cause)
                }
            }
        }

        override suspend fun fetchMovieDetails(id: Int): DataResult<DataMovieDetails> {
            return withContext(dispatcher) {
                try {
                    DataResult.Success(remoteDataSource.fetchMovieDetails(id).toDataMovieDetails())
                } catch (e: IOException) {
                    DataResult.Error(e.localizedMessage, e.cause)
                }
            }
        }
    }
