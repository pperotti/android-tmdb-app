package com.pperotti.android.moviescatalogapp.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pperotti.android.moviescatalogapp.domain.common.DomainResult
import com.pperotti.android.moviescatalogapp.domain.usecase.DomainMovieListResult
import com.pperotti.android.moviescatalogapp.domain.usecase.GetLatestMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        val getLatestMovies: GetLatestMovies,
    ) : ViewModel() {
        // A Job is required so you can cancel a running coroutine
        private var fetchJob: Job? = null

        // StateFlow to hold the UI state
        private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
        val uiState: StateFlow<MainUiState> get() = _uiState

        // Request items from repository and convert them to UI items
        fun requestData() {
            fetchJob?.cancel()
            fetchJob =
                viewModelScope.launch {
                    // Indicates the UI that loading should be presented
                    _uiState.value = MainUiState.Loading

                    when (val domainResponse = getLatestMovies.getLatestMovies()) {
                        is DomainResult.Success ->
                            transformDomainResultIntoUiResult(domainResponse.result)

                        is DomainResult.Error -> {
                            _uiState.value =
                                MainUiState.Error(
                                    domainResponse.message,
                                )
                        }
                    }
                }
        }

        private fun transformDomainResultIntoUiResult(domainMovieListResult: DomainMovieListResult) {
            val resultList: MutableList<MainListItemUiState> = mutableListOf()
            domainMovieListResult.results.forEach { movie ->
                resultList.add(
                    MainListItemUiState(
                        id = movie.id,
                        title = movie.title,
                        overview = movie.overview,
                        popularity = movie.popularity,
                        posterPath = "https://image.tmdb.org/t/p/original/${movie.posterPath}",
                    ),
                )
            }

            // Publish Items to the UI
            _uiState.value = MainUiState.Success(items = resultList)
        }
    }
