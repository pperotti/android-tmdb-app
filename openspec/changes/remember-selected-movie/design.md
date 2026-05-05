## Context

The movie catalog app displays a list of movies on the main screen. Users can select a movie to view its details. Currently, when users navigate back from the movie details screen, the previously selected movie is not visually highlighted, making it difficult to maintain context in the list.

## Goals / Non-Goals

**Goals:**
- Persist the selected movie ID during navigation to movie details
- Visually highlight the selected movie when returning to the main list
- Maintain selection state only during the current app session

**Non-Goals:**
- Persist selection across app restarts or backgrounding
- Store selection in persistent storage
- Modify the movie details screen behavior

## Decisions

**UI State Management**: Store the selected movie ID in the MainViewModel's UI state alongside the movie list data. This ensures the selection is part of the reactive state flow and automatically updates the UI when changed.

**Visual Highlighting**: Use a different background color or border for the selected movie card in the LazyVerticalGrid. The selection will be applied at the composable level using the movie ID comparison.

**Selection Reset**: Clear the selection when the user performs pull-to-refresh or when new data is loaded, ensuring the selection doesn't persist inappropriately across data changes.

## Risks / Trade-offs

- **UI Performance**: Adding selection highlighting to each movie card could have minimal performance impact on large lists → Mitigation: Use efficient conditional styling that only affects the selected item
- **State Complexity**: Adding selection state increases ViewModel complexity → Mitigation: Keep selection logic simple and contained within the existing UI state structure