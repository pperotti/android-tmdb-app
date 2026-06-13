## Why

The movie details screen currently displays the revenue as a raw integer (e.g. `462549154`), which is hard to read and inconsistent with localized currency display. Formatting revenue as a localized currency string (thousands separators, currency symbol, appropriate zero/unknown handling) improves readability and user experience.

## What Changes

- Add a new revenue formatting utility (presentation/util) that converts Long/null revenue values into localized currency strings.
- Update the details presentation to use the formatter instead of calling toString() on the revenue value.
- Add unit tests for the formatter and presentation-level tests verifying the formatted output.
- Update or add string resources if needed for localization.

## Capabilities

### New Capabilities
- `revenue-formatter`: Format movie revenue (Long?/nullable) into a localized currency string. Covers grouping, currency symbol, locale-aware formatting, and graceful handling of zero or unknown values. Exposes a simple API (e.g., `formatRevenue(value: Long?, locale: Locale = default): String`).

### Modified Capabilities
- `details-ui`: Display requirement change — revenue must be shown as a formatted currency string rather than a raw integer. (This is a UI-level requirement change, not a backend data model change.)

## Impact

Affected code and files (approximate):
- app/src/main/java/com/pperotti/android/moviescatalogapp/presentation/details/DetailsScreen.kt (replace revenue.toString() with formatted string)
- app/src/main/java/com/pperotti/android/moviescatalogapp/presentation/details/DetailsViewModel.kt (no semantic change required; UI will consume formatted string)
- app/src/main/java/com/pperotti/android/moviescatalogapp/presentation/details/DetailsUiState.kt (may add formattedRevenue: String or let the composable call the formatter)
- app/src/main/java/com/pperotti/android/moviescatalogapp/presentation/common/RevenueFormatter.kt (new utility)
- app/src/test/java/... Unit tests for RevenueFormatter and presentation tests verifying displayed text

This is a non-breaking change (presentation-only). The data model (domain/data) remains unchanged.
