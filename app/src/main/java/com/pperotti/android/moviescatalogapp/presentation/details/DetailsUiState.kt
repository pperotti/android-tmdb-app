package com.pperotti.android.moviescatalogapp.presentation.details

// State used for the UI drawing the details
sealed class DetailsUiState(val id: Int) {
    object Idle : DetailsUiState(-1)
    data class Loading(val requestId: Int) : DetailsUiState(requestId)
    data class Success(val requestId: Int, val details: DetailsUiData) : DetailsUiState(requestId)
    data class Error(val requestId: Int, val message: String?) : DetailsUiState(requestId)
}

// Data Container with the information relevant to the UI
data class DetailsUiData(
    val id: Int,
    val imdbId: String?,
    val homepage: String?,
    val overview: String?,
    val posterPath: String?,
    val genres: List<DetailsUiGenre>,
    val title: String?,
    val revenue: Long,
    val status: String?,
    val voteAverage: Float?,
    val voteCount: Int
)

// Data Container for genre used by the UI
data class DetailsUiGenre(
    val id: Int,
    val name: String?
)
