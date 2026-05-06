## Why

After introducing the remember-selected-movie feature, users navigate back to the movie list from the details page only to find the list scrolled to the top. This breaks the user experience where the previously viewed movie should remain visible and highlighted in the list.

## What Changes

- Capture the current scroll position in the list when navigating away to details
- Restore the exact scroll position when returning from details
- Ensure the previously selected movie item is highlighted during scroll restoration
- Maintain scroll state across navigation back/forward cycles

## Capabilities

### New Capabilities
- `list-scroll-position`: Restores and maintains the movie list's scroll position when navigating between list and details screens

### Modified Capabilities
<!-- Existing capabilities whose REQUIREMENTS are changing (not just implementation).
     Only list here if spec-level behavior changes. Each needs a delta spec file.
     Use existing spec names from openspec/specs/. Leave empty if no requirement changes. -->

## Impact

- Movie list UI component (scroll state management)
- Navigation lifecycle (state preservation during transitions)
- Integration with existing selected-movie-persistence feature
