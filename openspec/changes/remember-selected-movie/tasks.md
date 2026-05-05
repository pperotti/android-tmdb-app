## 1. UI State Management

- [ ] 1.1 Add selectedMovieId field to MainUiState data class
- [ ] 1.2 Update MainViewModel to initialize selectedMovieId as null
- [ ] 1.3 Add method to update selected movie ID in ViewModel

## 2. Selection Handling

- [ ] 2.1 Modify MainScreen to pass selected movie ID to CardItemComposable
- [ ] 2.2 Update CardItemComposable to accept and use selectedMovieId parameter
- [ ] 2.3 Implement visual highlighting for selected movie card

## 3. Navigation Integration

- [ ] 3.1 Update onMovieSelected callback to set selected movie ID in ViewModel
- [ ] 3.2 Ensure selected movie ID persists when navigating to details and back

## 4. Selection Reset

- [ ] 4.1 Clear selected movie ID when pull-to-refresh is triggered
- [ ] 4.2 Clear selected movie ID when new data is loaded (page 1 refresh)