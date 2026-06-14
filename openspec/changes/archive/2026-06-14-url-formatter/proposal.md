## Why

The movie details screen sometimes shows external URLs (e.g., official website, trailers) that are not presented consistently and are not easily opened by users. Introducing a URL formatter will standardize how URLs are shown, make them tappable in the UI, and ensure tapping opens the device's default browser. This improves discoverability and reduces friction when users want to view external content.

## What Changes

- Add a reusable URL formatter utility for presentation layer that converts raw URL strings into display-ready text and clickable UI elements.
- Update the Movie Details screen to use the URL formatter for any external links (official site, homepage, external reviews/trailers links exposed by the API).
- Ensure tapping a formatted URL launches the device default browser using a safe, validated external intent.
- Add unit tests for the formatter and integration tests verifying clickable behavior on the details screen.

## Capabilities

### New Capabilities
- `url-formatter`: A presentation-layer capability that formats and renders external URLs and exposes a standard onClick handler which opens the default browser with validated URLs.

### Modified Capabilities
- `movie-details`: (non-breaking) The Movie Details screen will consume the `url-formatter` capability to render external links. No change to domain-level models or repository APIs is expected.

## Impact

- Code: Changes in presentation module only (UI components for Movie Details, a new utility class/file under presentation/common or presentation/ui). Small additions to tests under app/src/test and instrumented tests if needed.
- APIs: No API contract changes. Behavior is purely client-side rendering and navigation to external URLs.
- Dependencies: No new external libraries required; will use standard Android Intent (ACTION_VIEW) mechanisms and existing Compose/View components.
- Accessibility & Safety: URLs will be validated and presented with accessible labels. External navigation will be explicit and follow existing app patterns for leaving the app (no in-app webview by default).



