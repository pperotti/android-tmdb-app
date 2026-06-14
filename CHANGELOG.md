# Changelog

## Unreleased

- Add revenue formatting presentation test and update changelog for `add-movie-revenue-formatter` openspec change.
- Add `url-formatter` capability (presentation):
  - New FormattedUrl data class and UrlFormatter.parse() to validate and format external URLs (http/https).
  - LinkMessageItemComposable to render tappable links in Movie Details and open default browser via Intent.
  - Metrics hooks for invalid URLs, no activity handler, and launch failures.
  - Unit tests and instrumented Espresso-Intents tests added.
  - AndroidManifest updated with <queries> for http/https to restore resolveActivity behavior on modern Android.

