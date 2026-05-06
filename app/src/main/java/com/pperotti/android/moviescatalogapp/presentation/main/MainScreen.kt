@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.pperotti.android.moviescatalogapp.presentation.main

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.pperotti.android.moviescatalogapp.R
import com.pperotti.android.moviescatalogapp.presentation.common.ErrorContent
import com.pperotti.android.moviescatalogapp.presentation.common.LoadingContent
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel(),
    onMovieSelected: (id: Int) -> Unit,
) {
    val context = LocalContext.current
    val state by mainViewModel.uiState.collectAsState()
    val currentScrollPosition = mainViewModel.getListScrollPosition()

    // Invoke fetchData only when the screen is still loading.
    LaunchedEffect(state) {
        if (state is MainUiState.Loading) {
            mainViewModel.requestData()
        }
    }

    LaunchedEffect(mainViewModel.uiEvents) {
        mainViewModel.uiEvents.collect { event ->
            when (event) {
                is UiEvent.ShowNoMoreDataToast ->
                    Toast.makeText(context, "No more data available", Toast.LENGTH_SHORT).show()
                is UiEvent.ShowErrorToast ->
                    Toast.makeText(context, event.message ?: "Unable to load more movies", Toast.LENGTH_SHORT).show()
            }
        }
    }

    DrawScreenContent(
        uiState = state,
        modifier = modifier,
        initialScrollPosition = currentScrollPosition,
        onScrollPositionChanged = { index, offset ->
            mainViewModel.saveScrollPosition(index, offset)
        },
        onMovieSelected = { id ->
            mainViewModel.selectMovie(id)
            onMovieSelected(id)
        },
        onPullToRefresh = {
            mainViewModel.requestData(forceRefresh = true)
        },
        onLoadMore = {
            mainViewModel.requestNextPage()
        },
    )
}

@Composable
fun DrawScreenContent(
    uiState: MainUiState,
    modifier: Modifier,
    initialScrollPosition: ListScrollPosition?,
    onScrollPositionChanged: (firstVisibleItemIndex: Int, firstVisibleItemScrollOffset: Int) -> Unit,
    onMovieSelected: (id: Int) -> Unit,
    onPullToRefresh: () -> Unit,
    onLoadMore: () -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { MainScreenTopAppBar(modifier) },
    ) { paddingValues ->
        when (uiState) {
            is MainUiState.Loading -> LoadingContent(modifier)
            is MainUiState.Success ->
                if (uiState.items.isEmpty()) {
                    EmptyContent(
                        modifier = modifier.padding(paddingValues),
                        onRefresh = onPullToRefresh,
                    )
                } else {
                    MainListContent(
                        modifier = modifier.padding(paddingValues),
                        uiItems = uiState.items,
                        currentPage = uiState.currentPage,
                        totalPages = uiState.totalPages,
                        isLoadingMore = uiState.isLoadingMore,
                        selectedMovieId = uiState.selectedMovieId,
                        onMovieSelected = onMovieSelected,
                        onPullToRefresh = onPullToRefresh,
                        onLoadMore = onLoadMore,
                        initialScrollPosition = initialScrollPosition,
                        onScrollPositionChanged = onScrollPositionChanged,
                    )
                }

            is MainUiState.Error ->
                ErrorContent(
                    modifier = modifier,
                    message = uiState.message,
                )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainListContent(
    uiItems: List<MainListItemUiState>,
    currentPage: Int,
    totalPages: Int,
    isLoadingMore: Boolean,
    selectedMovieId: Int?,
    modifier: Modifier,
    onMovieSelected: (id: Int) -> Unit,
    onPullToRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    initialScrollPosition: ListScrollPosition?,
    onScrollPositionChanged: (firstVisibleItemIndex: Int, firstVisibleItemScrollOffset: Int) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val columnSize = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1
    val gridState = rememberLazyGridState(
        initialFirstVisibleItemIndex = initialScrollPosition?.firstVisibleItemIndex ?: 0,
        initialFirstVisibleItemScrollOffset = initialScrollPosition?.firstVisibleItemScrollOffset ?: 0,
    )

    var isRefreshInProgress by remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()

    val shouldLoadMore = remember(gridState, currentPage, totalPages, isLoadingMore) {
        derivedStateOf {
            val lastVisibleItemIndex = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val totalItemCount = gridState.layoutInfo.totalItemsCount
            lastVisibleItemIndex == totalItemCount - 1 && totalItemCount > 0
        }
    }

    LaunchedEffect(gridState) {
        snapshotFlow { gridState.firstVisibleItemIndex to gridState.firstVisibleItemScrollOffset }
            .distinctUntilChanged()
            .collect { (index, offset) ->
                onScrollPositionChanged(index, offset)
            }
    }

    LaunchedEffect(shouldLoadMore.value, isLoadingMore, currentPage, totalPages) {
        if (shouldLoadMore.value && !isLoadingMore && currentPage < totalPages) {
            onLoadMore()
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshInProgress,
        onRefresh = {
            isRefreshInProgress = true
            coroutineScope.launch {
                onPullToRefresh()
                isRefreshInProgress = false
            }
        },
    ) {
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(columnSize),
            contentPadding = PaddingValues(16.dp),
            modifier = modifier.fillMaxSize(),
        ) {
            items(uiItems) { item ->
                CardItemComposable(
                    item,
                    selectedMovieId = selectedMovieId,
                    onMovieSelected = {
                        onScrollPositionChanged(
                            gridState.firstVisibleItemIndex,
                            gridState.firstVisibleItemScrollOffset,
                        )
                        onMovieSelected(it)
                    },
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyContent(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
) {
    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = onRefresh,
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.main_list_empty_state_message),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRefresh) {
                Text(text = stringResource(id = R.string.main_list_empty_state_refresh))
            }
        }
    }
}

@Suppress("MagicNumber")
@Composable
fun CardItemComposable(
    item: MainListItemUiState,
    selectedMovieId: Int?,
    onMovieSelected: (id: Int) -> Unit,
) {
    val isSelected = selectedMovieId == item.id
    Card(
        modifier =
            Modifier
                .padding(12.dp)
                .fillMaxSize()
                .border(
                    width = if (isSelected) 3.dp else 0.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    shape = RoundedCornerShape(16.dp),
                ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = {
            onMovieSelected(item.id)
        },
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model =
                    ImageRequest.Builder(LocalContext.current)
                        .data(item.posterPath)
                        .crossfade(true)
                        .build(),
                contentDescription = item.title,
                modifier =
                    Modifier
                        .fillMaxWidth(0.3f)
                        .fillMaxHeight(),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = item.title ?: "-",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
                Spacer(
                    modifier =
                        Modifier
                            .background(color = MaterialTheme.colorScheme.primary)
                            .fillMaxWidth()
                            .height(2.dp),
                )
                Text(text = "Rating: ${item.popularity}")
                Text(
                    text = item.overview ?: "-",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenTopAppBar(modifier: Modifier) {
    TopAppBar(
        title = {
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(id = R.string.main_list_top_bar_title),
                    modifier = modifier,
                    fontSize = 20.sp,
                )
            }
        },
        colors =
            TopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                scrolledContainerColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        modifier = modifier.fillMaxWidth(),
    )
}
