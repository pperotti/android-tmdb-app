# revenue-formatter specification

## Purpose

Provide a locale-aware formatter for movie revenue values and ensure the UI displays revenue using the formatter. (TBD)

## Requirements

## ADDED Requirements

### Requirement: Format revenue as localized currency
The system SHALL format movie revenue values (Long or null) into locale-aware currency strings, including proper thousands separators and currency symbol placement.

#### Scenario: Non-null revenue displayed
- **WHEN** the UI receives a non-null revenue value (e.g., 462549154)
- **THEN** the formatter SHALL return a string with the appropriate currency symbol and grouping for the user's locale (e.g., "$462,549,154" for en_US)

#### Scenario: Null revenue displayed
- **WHEN** the UI receives a null revenue value
- **THEN** the formatter SHALL return a localized placeholder string such as "N/A" or the localized equivalent

#### Scenario: Zero revenue displayed
- **WHEN** the UI receives a revenue value of 0
- **THEN** the formatter SHALL return an appropriately formatted zero value (e.g., "$0" or localized equivalent)

### Requirement: Configurable locale
The formatter SHALL accept an optional Locale parameter to override the default device locale.

#### Scenario: Locale override
- **WHEN** a specific locale is provided (e.g., Locale("es", "ES"))
- **THEN** the output SHALL reflect that locale's currency format (e.g., "462.549.154 €" for es_ES)

## MODIFIED Requirements

### Requirement: details-ui displays revenue formatted
The details UI SHALL display revenue using the revenue-formatter instead of raw integer values.

#### Scenario: Details screen shows formatted revenue
- **WHEN** the details screen renders
- **THEN** the revenue label/value pair SHALL show the formatted currency string produced by the revenue-formatter
