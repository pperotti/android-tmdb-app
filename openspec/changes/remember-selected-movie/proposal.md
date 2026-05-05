## Why

Users can lose their place in the movie list when navigating to movie details and returning. This creates a poor user experience as they have to scroll back to find their previously selected movie. Remembering the selected movie provides better navigation continuity and user satisfaction.

## What Changes

- Add persistence of the selected movie ID in the main screen's UI state
- Restore the selected movie highlight when returning from movie details
- Maintain selection state across app navigation

## Capabilities

### New Capabilities
- `selected-movie-persistence`: Capability to remember and restore the selected movie when returning to the main movie list

### Modified Capabilities
<!-- No existing capabilities are modified -->

## Impact

- Main screen UI state management
- Movie selection handling in MainViewModel
- Potential storage of selection state (if needed across app sessions)