## 1. Implementation

- [x] 1.1 Create presentation/common/RevenueFormatter.kt with a function `formatRevenue(value: Long?, locale: Locale? = null): String` and an interface for DI/testing.
- [x] 1.2 Add unit tests for RevenueFormatter covering non-null, null, zero, and locale override scenarios.
- [x] 1.3 Update DetailsScreen.kt to use RevenueFormatter when rendering revenue (replace `.toString()` usage).
- [x] 1.4 Update or add localized string resource for null/unknown revenue (e.g., `details_revenue_unknown`).

## 2. Validation & QA

- [x] 2.1 Run unit tests and fix any issues.
- [ ] 2.2 Manual UI check on emulator/device for at least en_US and es_ES locales.
- [ ] 2.3 Add a small presentation-level test or snapshot verifying the formatted string appears on DetailsScreen.

## 3. Release

- [ ] 3.1 Bump changelog/notes describing the UI improvement.
- [ ] 3.2 Create a PR with description linking to openspec change `add-movie-revenue-formatter` and include screenshots.
