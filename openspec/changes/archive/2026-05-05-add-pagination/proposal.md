## Why

The app currently loads a fixed movie list without pagination, which can cause slow initial load times and poor user experience when browsing large TMDB datasets. Adding pagination improves performance, reduces network usage, and enables smoother scrolling through more movies.

## What Changes

- Add pagination support for movie list retrieval in the TMDB data layer.
- Expose paginated movie-loading behavior to the app's presentation layer and UI.
- Update data models and repository flow to request subsequent pages as users scroll.
- Ensure pagination handles loading state, errors, and end-of-list conditions.

## Capabilities

### New Capabilities
- `pagination`: Provide paginated movie list retrieval and navigation, including page requests, incremental results, and end-of-list handling.

### Modified Capabilities
- 

## Impact

- Affected code: TMDB service client, movie remote/local data sources, repository, use cases for latest movies, and presentation/UI layers.
- May require updates to domain models, state handling, and UI logic for infinite scrolling or paged list display.
