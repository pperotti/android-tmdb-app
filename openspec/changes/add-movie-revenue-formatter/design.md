## Context

The app currently displays movie revenue as a raw integer in the details screen. The proposal and specs require formatting revenue into localized currency strings. This change is presentation-only and does not alter domain or data layers.

## Goals / Non-Goals

**Goals:**
- Provide a reusable, testable RevenueFormatter utility that formats nullable Long revenue values into localized currency strings.
- Integrate formatter into DetailsScreen composable to display formatted revenue.
- Add unit tests for formatting logic and presentation-level checks for the details UI.

**Non-Goals:**
- Change backend APIs or persist formatted strings in models.
- Introduce new third-party localization libraries; prefer platform/currency formatting APIs (java.text.NumberFormat / android.icu.text.NumberFormat).

## Decisions

1. Implementation location: presentation/common/RevenueFormatter.kt
   - Rationale: presentation layer handles formatting concerns; keeps domain/data unchanged.
   - Alternative: Add formatting in ViewModel, but that mixes UI concerns into state logic.

2. Use platform NumberFormat for locale-aware currency formatting.
   - Rationale: Built-in APIs handle locale, currency symbols, and grouping reliably.
   - Alternative: Use manual formatting or external libraries — avoided to reduce dependencies.

3. API surface: provide a top-level function and an injectable interface for testing.
   - Example: fun formatRevenue(value: Long?, locale: Locale? = null): String
   - Also provide an interface RevenueFormatter with a default implementation for DI if needed.
  
4. Preferred placeholder for null revenue
   - When the formatter receives 'null' as revenue value, apply the formatter to value 0L.

## Risks / Trade-offs

- Relying on device locale may show different currency symbols than the movie's production currency; this is acceptable for UI display.
- ICU/NumberFormat behavior varies slightly across Android versions; include unit tests covering common locales to guard against regressions.

## Migration Plan

- Implement formatter and tests.
- Update DetailsScreen to call formatter.
- Run UI preview / manual check on a few locales.
