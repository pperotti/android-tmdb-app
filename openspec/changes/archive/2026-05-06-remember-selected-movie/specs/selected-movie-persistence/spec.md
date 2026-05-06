## ADDED Requirements

### Requirement: Remember selected movie on navigation
The system SHALL remember the selected movie when navigating to movie details and restore the selection when returning to the main movie list.

#### Scenario: Selected movie remains highlighted after navigation
- **WHEN** user selects a movie from the main list
- **AND** navigates to the movie details screen
- **AND** returns to the main movie list
- **THEN** the previously selected movie remains visually highlighted in the list

#### Scenario: Selection cleared on data refresh
- **WHEN** user performs pull-to-refresh on the main movie list
- **THEN** any existing movie selection is cleared
- **THEN** no movie appears highlighted in the refreshed list

#### Scenario: Selection maintained during pagination
- **WHEN** user has a movie selected
- **AND** loads additional pages of movies
- **THEN** the selected movie remains highlighted
- **THEN** the selection is not affected by new movie items being added to the list