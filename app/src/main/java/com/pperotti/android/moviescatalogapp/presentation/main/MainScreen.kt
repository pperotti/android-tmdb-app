package com.pperotti.android.moviescatalogapp.presentation.main

import android.content.res.Configuration
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
    // Invoke fetchData when the screen is first displayed
    LaunchedEffect(true) {
        mainViewModel.requestData()
    }

    // Collect data from the ViewModel and react to it
    val state = mainViewModel.uiState.collectAsState().value
    DrawScreenContent(
        uiState = state,
        modifier = modifier,
        onMovieSelected = onMovieSelected,
        onPullToRefresh = {
            mainViewModel.requestData(forceRefresh = true)
        },
    )
}

@Composable
fun DrawScreenContent(
    uiState: MainUiState,
    modifier: Modifier,
    onMovieSelected: (id: Int) -> Unit,
    onPullToRefresh: () -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { MainScreenTopAppBar(modifier) },
    ) { paddingValues ->
        when (uiState) {
            is MainUiState.Loading -> LoadingContent(modifier)
            is MainUiState.Success ->
                MainListContent(
                    modifier = modifier.padding(paddingValues),
                    uiItems = uiState.items,
                    onMovieSelected = onMovieSelected,
                    onPullToRefresh = onPullToRefresh,
                )

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
    modifier: Modifier,
    onMovieSelected: (id: Int) -> Unit,
    onPullToRefresh: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val columnSize = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1

    var isRefreshInProgress by remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()

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
        // Display the appropriate content based on the UI state
        LazyVerticalGrid(
            columns = GridCells.Fixed(columnSize),
            contentPadding = PaddingValues(16.dp),
            modifier = modifier.fillMaxSize(),
        ) {
            items(uiItems) { item ->
                CardItemComposable(item, onMovieSelected = onMovieSelected)
                Spacer(modifier = Modifier.height(16.dp)) // Add space between cards
            }
        }
    }
}

@Suppress("MagicNumber")
@Composable
fun CardItemComposable(
    item: MainListItemUiState,
    onMovieSelected: (id: Int) -> Unit,
) {
    Card(
        modifier =
            Modifier
                .padding(12.dp)
                .fillMaxSize(),
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
