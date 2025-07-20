package com.pperotti.android.moviescatalogapp.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pperotti.android.moviescatalogapp.domain.common.DomainResult
import com.pperotti.android.moviescatalogapp.domain.usecase.DomainMovieDetails
import com.pperotti.android.moviescatalogapp.domain.usecase.GetMovieDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel
    @Inject
    constructor(
        val getMovieDetails: GetMovieDetails,
    ) : ViewModel() {
        // A Job is required so you can cancel a running coroutine
        private var fetchJob: Job? = null

        // StateFlow to hold the UI state
        private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
        val uiState: StateFlow<DetailsUiState> get() = _uiState

        // Fetch Details
        fun requestDetails(id: Int) {
            fetchJob?.cancel()
            fetchJob =
                viewModelScope.launch {
                    // Indicates the UI that loading should be presented
                    _uiState.value = DetailsUiState.Loading

                    // Retrieves the data
                    when (val domainResult = getMovieDetails.getMovieDetails(id)) {
                        is DomainResult.Success ->
                            transformSuccessResponse(domainResult.result)

                        is DomainResult.Error -> {
                            _uiState.value =
                                DetailsUiState.Error(
                                    message = domainResult.message,
                                )
                        }
                    }
                }
        }

        override fun onCleared() {
            super.onCleared()
            fetchJob?.cancel()
        }

        private fun transformSuccessResponse(domainMovieDetails: DomainMovieDetails) {
            val detailsUiData =
                DetailsUiData(
                    id = domainMovieDetails.id,
                    imdbId = domainMovieDetails.imdbId,
                    homepage = domainMovieDetails.homepage,
                    overview = domainMovieDetails.overview,
                    posterPath = domainMovieDetails.posterPath,
                    genres = domainMovieDetails.genres.map { DetailsUiGenre(it.id, it.name) },
                    title = domainMovieDetails.title,
                    revenue = domainMovieDetails.revenue,
                    status = domainMovieDetails.status,
                    voteAverage = domainMovieDetails.voteAverage,
                    voteCount = domainMovieDetails.voteCount,
                )

            // Publish items tot he UI
            _uiState.value =
                DetailsUiState.Success(
                    details = detailsUiData,
                )
        }
    }
