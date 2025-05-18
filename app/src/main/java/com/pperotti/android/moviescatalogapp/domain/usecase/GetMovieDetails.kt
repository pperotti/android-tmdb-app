package com.pperotti.android.moviescatalogapp.domain.usecase

import com.pperotti.android.moviescatalogapp.data.common.DataResult
import com.pperotti.android.moviescatalogapp.data.movie.DataMovieDetails
import com.pperotti.android.moviescatalogapp.data.movie.MovieRepository
import com.pperotti.android.moviescatalogapp.domain.common.DomainResult
import javax.inject.Inject

/**
 * Methods required to retrieve
 */
interface GetMovieDetails {

    /**
     * Retrieve the movie details from the repository
     * associated with this use case for the ID required
     *
     * @param Int The ID for the selected movie.
     */
    suspend fun getMovieDetails(id: Int): DomainResult<DomainMovieDetails>
}

/**
 * Default implementation for GetMovieDetails interface
 */
class DefaultGetMovieDetails @Inject constructor(
    val repository: MovieRepository
) : GetMovieDetails {

    override suspend fun getMovieDetails(id: Int): DomainResult<DomainMovieDetails> {
        return when (val dataDetailsResponse = repository.fetchMovieDetails(id)) {
            is DataResult.Success ->
                transformSuccessResponse(dataDetailsResponse.result)

            is DataResult.Error -> {
                DomainResult.Error(
                    message = dataDetailsResponse.message,
                    cause = dataDetailsResponse.cause
                )
            }
        }
    }

    private fun transformSuccessResponse(dataMovieDetails: DataMovieDetails): DomainResult.Success<DomainMovieDetails> {
        // Publish items tot he UI
        return DomainResult.Success(
            DomainMovieDetails(
                id = dataMovieDetails.id,
                imdbId = dataMovieDetails.imdbId,
                homepage = dataMovieDetails.homepage,
                overview = dataMovieDetails.overview,
                posterPath = "https://image.tmdb.org/t/p/w200/${dataMovieDetails.posterPath}",
                genres = dataMovieDetails.genres?.map { DomainMovieGenre(it.id, it.name) }
                    ?: emptyList(),
                title = dataMovieDetails.title,
                revenue = dataMovieDetails.revenue,
                status = dataMovieDetails.status,
                voteAverage = dataMovieDetails.voteAverage,
                voteCount = dataMovieDetails.voteCount,
            )
        )
    }
}

/**
 * Container for the details of the movie
 */
data class DomainMovieDetails(
    val id: Int,
    val imdbId: String?,
    val homepage: String?,
    val overview: String?,
    val posterPath: String?,
    val genres: List<DomainMovieGenre>,
    val title: String?,
    val revenue: Long,
    val status: String?,
    val voteAverage: Float?,
    val voteCount: Int
)

/**
 * Wrapper for movie genre
 */
data class DomainMovieGenre(
    val id: Int,
    val name: String?
)

