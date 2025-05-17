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