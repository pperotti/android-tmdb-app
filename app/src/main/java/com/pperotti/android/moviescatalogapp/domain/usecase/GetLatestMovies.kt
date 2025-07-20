package com.pperotti.android.moviescatalogapp.domain.usecase

import com.pperotti.android.moviescatalogapp.data.common.DataResult
import com.pperotti.android.moviescatalogapp.data.movie.DataMovieListResult
import com.pperotti.android.moviescatalogapp.data.movie.MovieRepository
import com.pperotti.android.moviescatalogapp.domain.common.DomainResult
import javax.inject.Inject

/**
 * Operations required to get the list of latest movies
 */
interface GetLatestMovies {
    /**
     * Retrieve a page with latest movies. If no page value
     * is provided, then the first page will be requested
     */
    suspend fun getLatestMovies(page: Int = 0): DomainResult<DomainMovieListResult>
}

/**
 * Default implementation for the GetLatestMovies interface
 */
class DefaultGetLatestMovies
    @Inject
    constructor(
        val repository: MovieRepository,
    ) : GetLatestMovies {
        override suspend fun getLatestMovies(page: Int): DomainResult<DomainMovieListResult> {
            return when (val movieResponse = repository.fetchMovieList()) {
                is DataResult.Success ->
                    transformDataResultIntoDomainResult(movieResponse.result)

                is DataResult.Error -> {
                    DomainResult.Error(
                        movieResponse.message,
                        movieResponse.cause,
                    )
                }
            }
        }

        private fun transformDataResultIntoDomainResult(dataMovieListResult: DataMovieListResult): DomainResult<DomainMovieListResult> {
            val resultList: MutableList<DomainMovieItem> = mutableListOf()
            dataMovieListResult.results.forEach { movie ->
                resultList.add(
                    DomainMovieItem(
                        id = movie.id,
                        title = movie.title,
                        overview = movie.overview,
                        popularity = movie.popularity,
                        posterPath = "https://image.tmdb.org/t/p/original/${movie.posterPath}",
                    ),
                )
            }

            // Publish Items to the UI
            return DomainResult.Success(
                DomainMovieListResult(
                    page = dataMovieListResult.page,
                    results = resultList,
                    totalPages = dataMovieListResult.totalPages,
                    totalResults = dataMovieListResult.totalResults,
                ),
            )
        }
    }

/**
 * Container for the list of movies from the domain's point of view
 */
data class DomainMovieListResult(
    val page: Int,
    val results: List<DomainMovieItem>,
    val totalPages: Int,
    val totalResults: Int,
)

/**
 * Movie Item from the domain's point of view
 */
data class DomainMovieItem(
    val id: Int,
    val title: String?,
    val overview: String?,
    val popularity: Float?,
    val posterPath: String?,
)
