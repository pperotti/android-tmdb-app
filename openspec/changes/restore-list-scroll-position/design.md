## Context

The movie list component uses RecyclerView to display paginated results. When users navigate to the movie details page and return, the RecyclerView resets to position 0, losing the user's place in the list. This was introduced after adding the remember-selected-movie feature but the scroll state preservation was not implemented alongside it.

The app uses Android Navigation Component for screen transitions and Jetpack ViewModel for state management across configuration changes.

## Goals / Non-Goals

**Goals:**
- Capture and persist the RecyclerView scroll position (first visible item index) when navigating away from the list
- Restore the exact scroll position when returning to the list screen
- Ensure the previously selected movie remains highlighted after scroll restoration
- Handle both smooth back navigation and recomposition due to configuration changes

**Non-Goals:**
- Changing how the selected movie is remembered (separate feature)
- Modifying pagination or data loading behavior
- Altering the navigation architecture

## Decisions

**1. Store scroll state in a dedicated ViewModel property**
- *Choice*: Use a ViewModel LiveData or StateFlow to hold `(firstVisibleItemIndex, firstVisibleItemOffset)`
- *Rationale*: Survives configuration changes and activity recreation; survives back navigation on the same NavGraph
- *Alternative considered*: SavedInstanceState only - insufficient for back navigation; Bundle arguments - loses state on config change

**2. Capture scroll position before navigation**
- *Choice*: Add a RecyclerView.OnScrollListener to capture position when user scrolls; also capture just before calling navigate()
- *Rationale*: Ensures position is saved before navigation event fires; prevents data race
- *Alternative considered*: Capture only on pause - misses rapid clicks; capture in onPause() only - timing issues with async transitions

**3. Restore scroll position after data load**
- *Choice*: Restore in RecyclerAdapter.onBindViewHolder or after observing list updates
- *Rationale*: Ensures RecyclerView is laid out and data is populated before restoring position
- *Alternative considered*: Restore immediately in onViewCreated - RecyclerView may not be laid out yet

**4. Handle edge cases**
- *Choice*: If saved position exceeds new list size, clamp to (size - 1); if list is empty, skip restoration
- *Rationale*: Prevents IndexOutOfBoundsException; handles pagination/filtering scenarios

## Risks / Trade-offs

**[Risk] Stale scroll position after data refresh**
- Occurs if list data changes significantly (filters, sorting, new data)
- *Mitigation*: Clear stored position when user changes filters or sorts; add flag to detect data-affecting operations

**[Risk] Restoration timing - scroll before layout complete**
- Calling scrollToPosition before RecyclerView layout pass may be ignored
- *Mitigation*: Use post() or ViewTreeObserver.OnPreDrawListener to delay restoration until after layout

**[Risk] Performance impact if list is very large**
- Restoring to a far position in a 10k-item list takes time
- *Mitigation*: Acceptable trade-off for UX; measure and optimize if profiling shows issues

**[Risk] Memory overhead**
- Storing scroll position adds minimal memory (two integers per list screen)
- *Mitigation*: Negligible; clear when screen is destroyed

## Migration Plan

1. Add scroll position properties to existing MovieListViewModel
2. Add RecyclerView scroll listener in MovieListFragment to capture position
3. Intercept navigation events to save position before navigating away
4. Restore position after data load (in data observer callback)
5. Test back navigation, configuration changes, and edge cases
6. No database migration or backwards compatibility concerns

## Open Questions

- Should scroll position be cleared when user manually refreshes the list (pull-to-refresh)?
- Should this feature be configurable/toggleable by the user?
- Do we need to restore offset or only item index?
