package com.pperotti.android.moviescatalogapp.data.movie

data class DataMovieListResult(
    val page: Int,
    val results: List<DataMovieItem>,
    val totalPages: Int,
    val totalResults: Int
)

data class DataMovieItem(
    val adult: Boolean?,
    val backdropPath: String?,
    val genreIds: List<Int>?,
    val id: Int,
    val originalLanguage: String?,
    val originalTitle: String?,
    val overview: String?,
    val popularity: Float?,
    val posterPath: String?,
    val releaseDate: String?,
    val title: String?,
    val video: Boolean?,
    val voteAverage: Float?,
    val voteCount: Int?
)

data class DataMovieCollection(
    val id: Int,
    val name: String?,
    val posterPath: String?,
    val backdropPath: String?
)

data class DataMovieGenre(
    val id: Int,
    val name: String?
)

data class DataProductionCompany(
    val id: Int,
    val logoPath: String?,
    val name: String?,
    val originCountry: String?
)

data class DataProductionCountry(
    val iso31661: String?,
    val name: String?
)

data class DataSpokenLanguage(
    val englishName: String?,
    val iso6391: String?,
    val name: String?
)

data class DataMovieDetails(
    val adult: Boolean,
    val backdropPath: String?,
    val belongsToCollection: DataMovieCollection?,
    val budget: Long?,
    val genres: List<DataMovieGenre>?,
    val homepage: String?,
    val id: Int,
    val imdbId: String?,
    val originCountry: List<String>?,
    val originalLanguage: String?,
    val originalTitle: String?,
    val overview: String?,
    val popularity: Float?,
    val posterPath: String?,
    val productionCompanies: List<DataProductionCompany>?,
    val productionCountries: List<DataProductionCountry>?,
    val releaseDate: String?,
    val revenue: Long,
    val runtime: Int,
    val spokenLanguages: List<DataSpokenLanguage>,
    val status: String?,
    val tagline: String?,
    val title: String?,
    val video: Boolean,
    val voteAverage: Float,
    val voteCount: Int
)
