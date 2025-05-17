package com.pperotti.android.moviescatalogapp.domain.usecase

import com.pperotti.android.moviescatalogapp.data.common.DataResult
import com.pperotti.android.moviescatalogapp.data.movie.MovieListResult
import com.pperotti.android.moviescatalogapp.data.movie.MovieRepository
import com.pperotti.android.moviescatalogapp.domain.common.DomainMovieItem
import com.pperotti.android.moviescatalogapp.domain.common.DomainMovieListResult
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
class DefaultGetLatestMovies @Inject constructor(
    val repository: MovieRepository
) : GetLatestMovies {

    override suspend fun getLatestMovies(page: Int): DomainResult<DomainMovieListResult> {
        return when (val movieResponse = repository.fetchMovieList()) {
            is DataResult.Success ->
                transformDataResultIntoDomainResult(movieResponse.result)
            is DataResult.Error -> {
                DomainResult.Error(
                    movieResponse.message, movieResponse.cause
                )
            }
        }
    }

    private fun transformDataResultIntoDomainResult(movieListResult: MovieListResult): DomainResult<DomainMovieListResult> {
        val resultList: MutableList<DomainMovieItem> = mutableListOf()
        movieListResult.results.forEach { movie ->
            resultList.add(
                DomainMovieItem(
                    id = movie.id,
                    title = movie.title,
                    overview = movie.overview,
                    popularity = movie.popularity,
                    posterPath = "https://image.tmdb.org/t/p/original/${movie.posterPath}"
                )
            )
        }

        // Publish Items to the UI
        return DomainResult.Success(
            DomainMovieListResult(
                page = movieListResult.page,
                results = resultList,
                totalPages = movieListResult.totalPages,
                totalResults = movieListResult.totalResults
            )
        )
    }

}