package com.pperotti.android.moviescatalogapp.presentation.main

/**
 * This is the state that represents what the screen will look like
 */
sealed class MainUiState {
    object Loading : MainUiState()

    data class Success(
        val items: List<MainListItemUiState>,
        val currentPage: Int = 1,
        val totalPages: Int = 1,
        val isLoadingMore: Boolean = false,
        val selectedMovieId: Int? = null,
    ) : MainUiState()

    data class Error(val message: String?) : MainUiState()
}

/**
 * This represents an item on the list
 */
data class MainListItemUiState(
    val id: Int,
    val title: String?,
    val overview: String?,
    val popularity: Float?,
    val posterPath: String?,
)
