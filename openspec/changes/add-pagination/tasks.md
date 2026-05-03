## 1. Data Layer

- [ ] 1.1 Extend `MovieRepository.fetchMovieList` to accept a page parameter and forward it to the remote data source.
- [ ] 1.2 Update `MovieRemoteDataSource` and its default implementation to fetch the requested TMDB page.
- [ ] 1.3 Update `MovieLocalDataSource` to persist paged movie results without overwriting all existing pages when loading subsequent pages.

## 2. Domain Layer

- [ ] 2.1 Ensure `GetLatestMovies.getLatestMovies` forwards the requested page to the repository.
- [ ] 2.2 Preserve page metadata (`page`, `totalPages`, `totalResults`) in the domain result.

## 3. Presentation Layer

- [ ] 3.1 Extend `MainViewModel` state to accumulate loaded movie pages and track current page, total pages, and load state.
- [ ] 3.2 Add logic to request the next page when the user scrolls near the end of the current list.
- [ ] 3.3 Add a bottom loading indicator for additional page loads.
- [ ] 3.4 Keep pull-to-refresh behavior but reset pagination to page 1 when triggered.

## 4. Validation

- [ ] 4.1 Add tests or manual validation scenarios for initial page load, next-page append, end-of-list handling, and refresh behavior.
- [ ] 4.2 Confirm that page load failures show proper error state without discarding already loaded items.