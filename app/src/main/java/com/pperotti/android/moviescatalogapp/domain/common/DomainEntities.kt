package com.pperotti.android.moviescatalogapp.domain.common

data class DomainMovieListResult(
    val page: Int,
    val results: List<DomainMovieItem>,
    val totalPages: Int,
    val totalResults: Int
)

data class DomainMovieItem(
    val id: Int,
    val title: String?,
    val overview: String?,
    val popularity: Float?,
    val posterPath: String?
)

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

data class DomainMovieGenre(
    val id: Int,
    val name: String?
)