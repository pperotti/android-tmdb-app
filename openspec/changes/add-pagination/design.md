## Context

The current app retrieves movies in a single dataset and caches them locally as one list. The domain interface already exposes a page parameter, but the repository and UI ignore it. The app presentation layer loads once on startup and only supports pull-to-refresh, so users cannot browse beyond the initial page of TMDB results.

## Goals / Non-Goals

**Goals:**
- Support paginated movie retrieval from TMDB using the existing `page` query parameter.
- Update the repository and local data source to preserve page-aware results rather than overwriting the single cached page.
- Add UI behavior to append next pages when the user scrolls near the end of the visible list.
- Maintain explicit loading and error states for both initial load and page load operations.

**Non-Goals:**
- Integrating Android Paging 3 or a third-party paging library.
- Implementing offline-first browsing of all previously loaded pages.
- Changing movie browsing behavior beyond the current discover/popularity flow.

## Decisions

- Keep the existing multi-layer architecture and extend it incrementally.
  - Use `GetLatestMovies.getLatestMovies(page: Int, forceRefresh: Boolean)` as the pagination entry point.
  - Update `MovieRepository.fetchMovieList` to accept and forward a page parameter to the remote data source.
- Implement manual pagination using Compose state and list append logic.
  - Continue using `LazyVerticalGrid` in the main screen.
  - Detect end-of-list scrolling and request the next page when the user is close to the visible end.
- Make local storage page-aware while preserving existing list semantics.
  - For page 1 refresh, replace stale cached pages and movie metadata.
  - For subsequent pages, append new movies and update pagination metadata.
- Avoid adding new runtime dependencies.
  - This behavior is achievable with existing repository, use case, and Compose primitives.

## Risks / Trade-offs

- [Risk] Scroll-end detection may request the same page multiple times if the load state is not guarded.
  - Mitigation: add a dedicated loading state and only request a page when not already loading and `page < totalPages`.
- [Risk] Local cache can become inconsistent if page data is stored and later refreshed out of order.
  - Mitigation: treat page 1 refresh as canonical and clear cache before reloading, while appending subsequent pages only after successful fetch.
- [Risk] The UI may appear to load slowly if additional page requests are triggered too early.
  - Mitigation: trigger next-page loads only when the user scrolls within a small threshold of the end and present a bottom loader state.

## Migration Plan

1. Update the data layer to accept page requests and persist page-aware results.
2. Update the domain use case to forward page numbers and return total page metadata.
3. Extend the main screen state to preserve an accumulated movie list and pagination metadata.
4. Add end-of-list detection and page-load handling to the main screen.
5. Validate with initial and subsequent page loads, pull-to-refresh, and error flows.

## Decisions Resolved

- Only when the user scrolls to the end (not prefetch).
- If the page is page 1, then display the empty state. If it is a subsequent page, then hide the loading spinner and present a toast saying "No more data available".
- Page size and discover filters will be treated as separate features in the future.