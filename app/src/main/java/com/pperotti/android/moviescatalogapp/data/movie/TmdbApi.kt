package com.pperotti.android.moviescatalogapp.data.movie

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("discover/movie?language=en-US&sort_by=popularity")
    suspend fun fetchMovieList(
        @Query("include_adult") adult: Boolean,
        @Query("include_video") video: Boolean,
        @Query("page") page: Int,
    ): RemoteMovieListResult

    @GET("movie/{id}")
    suspend fun fetchMovieDetails(
        @Path("id") id: Int,
    ): RemoteMovieDetails
}

/**
 * Represent the result of a query to TMDB api.
 *
 * ```json
 *     {
 *     "page": 1,
 *     "results": [
 *         {
 *             "adult": false,
 *             "backdrop_path": "/zOpe0eHsq0A2NvNyBbtT6sj53qV.jpg",
 *             "genre_ids": [
 *                 28,
 *                 878,
 *                 35,
 *                 10751
 *             ],
 *             "id": 939243,
 *             "original_language": "en",
 *             "original_title": "Sonic the Hedgehog 3",
 *             "overview": "Sonic, Knuckles, and Tails reunite against ...",
 *             "popularity": 4924.543,
 *             "poster_path": "/d8Ryb8AunYAuycVKDp5HpdWPKgC.jpg",
 *             "release_date": "2024-12-19",
 *             "title": "Sonic the Hedgehog 3",
 *             "video": false,
 *             "vote_average": 7.8,
 *             "vote_count": 1438
 *         },
 *         ...
 *     ],
 *     "total_pages": 48513,
 *     "total_results": 970247
 * }
 * ```
 */
data class RemoteMovieListResult(
    val page: Int,
    val results: List<RemoteMovieItem>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int,
)

/**
 * Represent an entry in the 'results' list.
 *
 * ```json
 *     {
 *     "adult": false,
 *     "backdrop_path": "/pqulyfkug9A7TmmRn5zrbRA8TAY.jpg",
 *     "genre_ids": [
 *         28,
 *         35
 *     ],
 *     "id": 1255788,
 *     "original_language": "fr",
 *     "original_title": "Le Jardinier",
 *     "overview": "Every year the Prime Minister has a list of all ...",
 *     "popularity": 1034.435,
 *     "poster_path": "/5T9WR7vIOnHm6xhVt5zBuPbBgt1.jpg",
 *     "release_date": "2025-01-30",
 *     "title": "The Gardener",
 *     "video": false,
 *     "vote_average": 6.135,
 *     "vote_count": 37
 * }
 * ```
 */
data class RemoteMovieItem(
    val adult: Boolean?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("genre_ids") val genreIds: List<Int>?,
    val id: Int,
    @SerializedName("original_language") val originalLanguage: String?,
    @SerializedName("original_title") val originalTitle: String?,
    val overview: String?,
    val popularity: Float?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    val title: String?,
    val video: Boolean?,
    @SerializedName("vote_average") val voteAverage: Float?,
    @SerializedName("vote_count") val voteCount: Int?,
)

/**
 * Represent the full details for a single item
 *
 * Example:
 * ```json
 *     {
 *     "adult": false,
 *     "backdrop_path": "/zOpe0eHsq0A2NvNyBbtT6sj53qV.jpg",
 *     "belongs_to_collection": {
 *         "id": 720879,
 *         "name": "Sonic the Hedgehog Collection",
 *         "poster_path": "/fwFWhYXj8wY6gFACtecJbg229FI.jpg",
 *         "backdrop_path": "/l5CIAdxVhhaUD3DaS4lP4AR2so9.jpg"
 *     },
 *     "budget": 122000000,
 *     "genres": [
 *         {
 *             "id": 28,
 *             "name": "Action"
 *         },
 *         .....
 *     ],
 *     "homepage": "https://www.sonicthehedgehogmovie.com",
 *     "id": 939243,
 *     "imdb_id": "tt18259086",
 *     "origin_country": [
 *         "US"
 *     ],
 *     "original_language": "en",
 *     "original_title": "Sonic the Hedgehog 3",
 *     "overview": "Sonic, Knuckles, and Tails reunite against a powerful new ...",
 *     "popularity": 4924.543,
 *     "poster_path": "/d8Ryb8AunYAuycVKDp5HpdWPKgC.jpg",
 *     "production_companies": [
 *         {
 *             "id": 4,
 *             "logo_path": "/gz66EfNoYPqHTYI4q9UEN4CbHRc.png",
 *             "name": "Paramount Pictures",
 *             "origin_country": "US"
 *         },
 *         ...
 *     ],
 *     "production_countries": [
 *         {
 *             "iso_3166_1": "US",
 *             "name": "United States of America"
 *         },
 *         ...
 *     ],
 *     "release_date": "2024-12-19",
 *     "revenue": 462549154,
 *     "runtime": 110,
 *     "spoken_languages": [
 *         {
 *             "english_name": "English",
 *             "iso_639_1": "en",
 *             "name": "English"
 *         }
 *         ...
 *     ],
 *     "status": "Released",
 *     "tagline": "Try to keep up.",
 *     "title": "Sonic the Hedgehog 3",
 *     "video": false,
 *     "vote_average": 7.8,
 *     "vote_count": 1438
 * }
 * ```
 */
data class RemoteMovieDetails(
    val adult: Boolean,
    @SerializedName("backdropPath") val backdropPath: String?,
    @SerializedName("belongs_to_collection") val belongsToCollection: RemoteMovieCollection?,
    val budget: Long?,
    val genres: List<RemoteMovieGenre>?,
    val homepage: String?,
    val id: Int,
    val imdbId: String?,
    @SerializedName("origin_country") val originCountry: List<String>?,
    @SerializedName("original_language") val originalLanguage: String?,
    @SerializedName("original_title") val originalTitle: String?,
    val overview: String?,
    val popularity: Float?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("production_companies") val productionCompanies: List<RemoteProductionCompany>?,
    @SerializedName("production_countries") val productionCountries: List<RemoteProductionCountry>?,
    @SerializedName("release_date") val releaseDate: String?,
    val revenue: Long,
    val runtime: Int,
    @SerializedName("spoken_languages") val spokenLanguages: List<RemoteSpokenLanguage>,
    val status: String?,
    val tagline: String?,
    val title: String?,
    val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Float,
    @SerializedName("vote_count") val voteCount: Int,
)

data class RemoteMovieCollection(
    val id: Int,
    val name: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
)

data class RemoteMovieGenre(
    val id: Int,
    val name: String?,
)

data class RemoteProductionCompany(
    val id: Int,
    @SerializedName("logo_path") val logoPath: String?,
    val name: String?,
    @SerializedName("origin_country") val originCountry: String?,
)

data class RemoteProductionCountry(
    @SerializedName("iso?3166_1") val iso31661: String?,
    val name: String?,
)

data class RemoteSpokenLanguage(
    @SerializedName("english_name") val englishName: String?,
    @SerializedName("iso_639_1") val iso6391: String?,
    val name: String?,
)

fun RemoteMovieListResult.toStorageMovieListResult(): StorageMovieListResult {
    return StorageMovieListResult(
        page = this.page,
        totalPages = this.totalPages,
        totalResults = this.totalResults,
    )
}

fun RemoteMovieItem.toStorageMovieItem(page: Int = 0): StorageMovie {
    return StorageMovie(
        id = this.id,
        adult = this.adult,
        backdropPath = this.backdropPath,
        genreIds = this.genreIds,
        originalLanguage = this.originalLanguage,
        originalTitle = this.originalTitle,
        overview = this.overview,
        popularity = this.popularity,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate,
        title = this.title,
        video = this.video,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        page = page,
    )
}

fun RemoteMovieCollection.toDataMovieCollection(): DataMovieCollection {
    return DataMovieCollection(
        id = this.id,
        name = this.name,
        posterPath = this.posterPath,
        backdropPath = this.backdropPath,
    )
}

fun RemoteMovieGenre.toDataMovieGenre(): DataMovieGenre {
    return DataMovieGenre(
        id = this.id,
        name = this.name,
    )
}

fun RemoteProductionCompany.toDataProductionCompany(): DataProductionCompany {
    return DataProductionCompany(
        id = this.id,
        logoPath = this.logoPath,
        name = this.name,
        originCountry = this.originCountry,
    )
}

fun RemoteProductionCountry.toDataProductionCountry(): DataProductionCountry {
    return DataProductionCountry(
        iso31661 = this.iso31661,
        name = this.name,
    )
}

fun RemoteSpokenLanguage.toDataSpokenLanguage(): DataSpokenLanguage {
    return DataSpokenLanguage(
        englishName = this.englishName,
        iso6391 = this.iso6391,
        name = this.name,
    )
}

fun RemoteMovieDetails.toDataMovieDetails(): DataMovieDetails {
    return DataMovieDetails(
        adult = this.adult,
        backdropPath = this.backdropPath,
        belongsToCollection = this.belongsToCollection?.toDataMovieCollection(),
        budget = this.budget,
        genres = this.genres?.map { it.toDataMovieGenre() },
        homepage = this.homepage,
        id = this.id,
        imdbId = this.imdbId,
        originCountry = this.originCountry,
        originalLanguage = this.originalLanguage,
        originalTitle = this.originalTitle,
        overview = this.overview,
        popularity = this.popularity,
        posterPath = this.posterPath,
        productionCompanies = this.productionCompanies?.map { it.toDataProductionCompany() },
        productionCountries = this.productionCountries?.map { it.toDataProductionCountry() },
        releaseDate = this.releaseDate,
        revenue = this.revenue,
        runtime = this.runtime,
        spokenLanguages = this.spokenLanguages.map { it.toDataSpokenLanguage() },
        status = this.status,
        tagline = this.tagline,
        title = this.title,
        video = this.video,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
    )
}
