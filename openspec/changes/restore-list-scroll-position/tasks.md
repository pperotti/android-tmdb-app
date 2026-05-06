## 1. Update MovieListViewModel

- [x] 1.1 Add LiveData/StateFlow properties to hold scroll position (firstVisibleItemIndex, firstVisibleItemOffset)
- [x] 1.2 Add method to save scroll position
- [x] 1.3 Add method to get stored scroll position
- [x] 1.4 Add method to clear scroll position
- [x] 1.5 Add flag to detect data-affecting operations (filter, sort, refresh)

## 2. Setup RecyclerView Scroll Listener

- [x] 2.1 Create custom RecyclerView.OnScrollListener in MovieListFragment
- [x] 2.2 Implement onScrolled() to capture scroll position on each scroll event
- [x] 2.3 Attach scroll listener to RecyclerView in onViewCreated()
- [x] 2.4 Store scroll position in ViewModel via the listener

## 3. Capture Position Before Navigation

- [x] 3.1 Hook into Navigation back press event handling
- [x] 3.2 Capture and save scroll position before navigate() call in details navigation
- [x] 3.3 Ensure position is saved before back navigation executes
- [ ] 3.4 Test rapid navigation to ensure position is always captured

## 4. Handle Filter and Sort Changes

- [ ] 4.1 Add observer to filter state changes
- [ ] 4.2 Clear scroll position when filter is applied
- [ ] 4.3 Add observer to sort state changes
- [ ] 4.4 Clear scroll position when sort order changes
- [x] 4.5 Implement pull-to-refresh callback to clear scroll position

## 5. Restore Scroll Position

- [x] 5.1 Add observer to list data changes in MovieListFragment
- [x] 5.2 Extract scroll position from ViewModel when data is loaded
- [x] 5.3 Implement scroll restoration logic that runs after layout
- [x] 5.4 Use ViewTreeObserver.OnPreDrawListener or post() to delay restoration
- [x] 5.5 Apply scroll position via RecyclerView.smoothScrollToPosition()

## 6. Edge Case Handling

- [x] 6.1 Implement position clamping (clamp to list.size - 1 if needed)
- [x] 6.2 Handle empty list scenario (skip restoration)
- [x] 6.3 Handle first-time launch (no stored position)
- [ ] 6.4 Test configuration change during navigation
- [ ] 6.5 Test configuration change during details view

## 7. Integration with Remember-Selected-Movie

- [ ] 7.1 Verify scroll position restoration preserves selected movie highlight
- [ ] 7.2 Test that both features work together without conflict
- [ ] 7.3 Ensure selected movie highlight is visible in restored scroll position
- [ ] 7.4 Handle scenario where selected movie is outside scroll range

## 8. Testing

- [ ] 8.1 Write unit tests for scroll position capture/restore ViewModel logic
- [ ] 8.2 Write instrumented test for scroll listener attachment
- [ ] 8.3 Write instrumented test for navigation back behavior
- [ ] 8.4 Test on different screen sizes and orientations
- [ ] 8.5 Test with various list sizes (small, medium, large)
- [ ] 8.6 Verify no memory leaks from scroll listener
- [ ] 8.7 Performance test: measure scroll restoration time

## 9. Code Review and Cleanup

- [ ] 9.1 Review and document scroll position capture/restore logic
- [ ] 9.2 Remove any debugging code or temporary implementations
- [ ] 9.3 Ensure error handling for edge cases
- [ ] 9.4 Add Javadoc/KDoc comments to key methods
- [ ] 9.5 Perform final testing pass
