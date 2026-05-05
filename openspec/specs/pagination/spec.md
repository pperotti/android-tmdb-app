# pagination Specification

## Purpose
TBD - created by archiving change add-pagination. Update Purpose after archive.
## Requirements
### Requirement: Paginated movie retrieval
The system SHALL allow the latest movie list to be requested by page number from the TMDB API.

#### Scenario: Request first page of latest movies
- **WHEN** the app loads the main screen for the first time
- **THEN** the system requests page 1 for latest movies
- **THEN** the UI displays the movies returned for page 1

#### Scenario: Request subsequent page when more pages exist
- **WHEN** the user scrolls near the end of the current movie list
- **AND** the current `page` is less than `totalPages`
- **THEN** the system requests the next page of movies
- **THEN** the UI appends the new page results to the existing list

### Requirement: Stop pagination at the last page
The system SHALL stop requesting new pages when the currently loaded page equals the total available pages.

#### Scenario: No more pages available
- **WHEN** the user scrolls to the end of the movie list
- **AND** the current `page` equals `totalPages`
- **THEN** the system does not request an additional page
- **THEN** the UI does not display a load-more indicator beyond the final list

### Requirement: Maintain separate loading states for initial and additional pages
The system SHALL expose distinct loading behavior for the initial page load and for loading additional pages.

#### Scenario: Initial page load displays top-level loading state
- **WHEN** the app starts and begins loading page 1
- **THEN** the UI displays the main loading indicator
- **THEN** the UI hides the movie grid until results arrive

#### Scenario: Additional page load displays a bottom loader
- **WHEN** the user requests page N+1 while page N is already visible
- **THEN** the UI displays a loading indication associated with the page append operation
- **THEN** the existing movie items remain visible during the additional load

### Requirement: Pull-to-refresh resets pagination
The system SHALL refresh the movie list by reloading page 1 and clearing any previously loaded pages.

#### Scenario: Pull-to-refresh resets data
- **WHEN** the user performs a pull-to-refresh gesture on the main movie list
- **THEN** the system requests page 1 with `forceRefresh = true`
- **THEN** the UI replaces the current list with the refreshed page 1 results

### Requirement: Persist page-aware movie list results locally
The system SHALL store movie list results with page metadata so cached data remains consistent with paginated behavior.

#### Scenario: Save paged movie results
- **WHEN** page N is successfully retrieved from the TMDB API
- **THEN** the system stores the returned items and page metadata in local storage
- **THEN** page-specific data can be used to restore the last cached state if needed

### Requirement: Handle page load failures gracefully
The system SHALL surface errors when a page fetch fails and allow the user to retry.

#### Scenario: Initial page load fails
- **WHEN** page 1 fetch fails due to network or server error
- **THEN** the UI displays an error message
- **THEN** the system does not display stale loaded items

#### Scenario: Subsequent page load fails
- **WHEN** page N+1 fetch fails while page N is already loaded
- **THEN** the UI preserves the already loaded items
- **THEN** the UI displays an inline error or retry option for loading more pages

