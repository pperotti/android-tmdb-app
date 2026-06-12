package com.pperotti.android.moviescatalogapp.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pperotti.android.moviescatalogapp.domain.common.DomainResult
import com.pperotti.android.moviescatalogapp.domain.usecase.DomainMovieListResult
import com.pperotti.android.moviescatalogapp.domain.usecase.GetLatestMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ListScrollPosition(
    val firstVisibleItemIndex: Int,
    val firstVisibleItemScrollOffset: Int,
)

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        val getLatestMovies: GetLatestMovies,
    ) : ViewModel() {
        // A Job is required so you can cancel a running coroutine
        private var fetchJob: Job? = null

        // State represents what the UI renders
        private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
        val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

        // Events represents one-time UI events like toasts or navigation
        private val _uiEvents = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
        val uiEvents: SharedFlow<UiEvent> = _uiEvents.asSharedFlow()

        private var currentItems: List<MainListItemUiState> = emptyList()
        private var currentPage = 1
        private var totalPages = 1
        private var isLoadingMore = false
        private var selectedMovieId: Int? = null
        private var listScrollPosition: ListScrollPosition? = null

        fun requestData(forceRefresh: Boolean = false) {
            if (forceRefresh) {
                selectedMovieId = null
                clearScrollPosition()
            }
            loadMovies(page = 1, forceRefresh = forceRefresh, append = false)
        }

        fun saveScrollPosition(
            firstVisibleItemIndex: Int,
            firstVisibleItemScrollOffset: Int,
        ) {
            listScrollPosition =
                ListScrollPosition(
                    firstVisibleItemIndex = firstVisibleItemIndex,
                    firstVisibleItemScrollOffset = firstVisibleItemScrollOffset,
                )
        }

        fun getListScrollPosition(): ListScrollPosition? = listScrollPosition

        fun clearScrollPosition() {
            listScrollPosition = null
        }

        fun requestNextPage() {
            if (isLoadingMore || currentPage >= totalPages) {
                return
            }
            loadMovies(page = currentPage + 1, forceRefresh = false, append = true)
        }

        fun selectMovie(movieId: Int) {
            selectedMovieId = movieId
            // Update the current state to reflect the selection
            val currentState = _uiState.value
            if (currentState is MainUiState.Success) {
                _uiState.value = currentState.copy(selectedMovieId = selectedMovieId)
            }
        }

        private fun loadMovies(
            page: Int,
            forceRefresh: Boolean,
            append: Boolean,
        ) {
            fetchJob?.cancel()
            fetchJob =
                viewModelScope.launch {
                    if (!append) {
                        selectedMovieId = null
                    }
                    if (append) {
                        _uiState.value =
                            MainUiState.Success(
                                items = currentItems,
                                currentPage = currentPage,
                                totalPages = totalPages,
                                isLoadingMore = true,
                                selectedMovieId = selectedMovieId,
                            )
                    } else {
                        _uiState.value = MainUiState.Loading
                    }

                    val domainResponse = getLatestMovies.getLatestMovies(page = page, forceRefresh = forceRefresh)

                    when (domainResponse) {
                        is DomainResult.Success -> handleLoadSuccess(domainResponse, append)
                        is DomainResult.Error -> handleLoadError(domainResponse, append)
                    }
                }
        }

        private fun handleLoadSuccess(domainResponse: DomainResult.Success<DomainMovieListResult>, append: Boolean) {
            val mergedItems = transformDomainResultIntoUiResult(domainResponse.result, append)
            currentItems = mergedItems
            currentPage = domainResponse.result.page
            totalPages = domainResponse.result.totalPages
            isLoadingMore = false

            _uiState.value =
                MainUiState.Success(
                    items = mergedItems,
                    currentPage = currentPage,
                    totalPages = totalPages,
                    isLoadingMore = false,
                    selectedMovieId = selectedMovieId,
                )

            if (append && domainResponse.result.results.isEmpty()) {
                _uiEvents.tryEmit(UiEvent.ShowNoMoreDataToast)
            }
        }

        private fun handleLoadError(domainResponse: DomainResult.Error<*>, append: Boolean) {
            if (append) {
                _uiState.value =
                    MainUiState.Success(
                        items = currentItems,
                        currentPage = currentPage,
                        totalPages = totalPages,
                        isLoadingMore = false,
                        selectedMovieId = selectedMovieId,
                    )
                _uiEvents.tryEmit(UiEvent.ShowErrorToast(domainResponse.message ?: "Unable to load more movies"))
            } else {
                _uiState.value = MainUiState.Error(domainResponse.message)
            }
        }

        private fun transformDomainResultIntoUiResult(
            domainMovieListResult: DomainMovieListResult,
            append: Boolean,
        ): List<MainListItemUiState> {
            val resultList =
                domainMovieListResult.results.map { movie ->
                    MainListItemUiState(
                        id = movie.id,
                        title = movie.title,
                        overview = movie.overview,
                        popularity = movie.popularity,
                        posterPath = "https://image.tmdb.org/t/p/original/${movie.posterPath}",
                    )
                }

            return if (append) {
                currentItems + resultList
            } else {
                resultList
            }
        }
    }

sealed class UiEvent {
    object ShowNoMoreDataToast : UiEvent()

    data class ShowErrorToast(val message: String) : UiEvent()
}
