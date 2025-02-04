package com.pperotti.android.moviescatalogapp.data.movie

import com.pperotti.android.moviescatalogapp.data.common.RepositoryResponse
import com.pperotti.android.moviescatalogapp.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
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
     */
    suspend fun fetchMovieList(): RepositoryResponse<MovieListResult>
}

// Default implementation
@Singleton
class DefaultMovieRepository @Inject constructor(
    val remoteDataSource: MovieRemoteDataSource,
    @IoDispatcher val dispatcher: CoroutineDispatcher
) : MovieRepository{
    override suspend fun fetchMovieList(): RepositoryResponse<MovieListResult> {
        return withContext(dispatcher) {
            try {
                RepositoryResponse.Success(remoteDataSource.fetchMovieList())
            } catch (e: Exception) {
                RepositoryResponse.Error(e.localizedMessage, e.cause)
            }
        }
    }
}
