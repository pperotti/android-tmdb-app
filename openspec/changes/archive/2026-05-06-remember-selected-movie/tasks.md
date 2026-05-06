## 1. UI State Management

- [x] 1.1 Add selectedMovieId field to MainUiState data class
- [x] 1.2 Update MainViewModel to initialize selectedMovieId as null
- [x] 1.3 Add method to update selected movie ID in ViewModel

## 2. Selection Handling

- [x] 2.1 Modify MainScreen to pass selected movie ID to CardItemComposable
- [x] 2.2 Update CardItemComposable to accept and use selectedMovieId parameter
- [x] 2.3 Implement visual highlighting for selected movie card

## 3. Navigation Integration

- [x] 3.1 Update onMovieSelected callback to set selected movie ID in ViewModel
- [x] 3.2 Ensure selected movie ID persists when navigating to details and back

## 4. Selection Reset

- [x] 4.1 Clear selected movie ID when pull-to-refresh is triggered
- [x] 4.2 Clear selected movie ID when new data is loaded (page 1 refresh)