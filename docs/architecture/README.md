# Architecture Documentation

This directory contains the technical architecture documentation for the TMDB Android Application.

## Contents

### Diagrams

1. **[Deployment Diagram](deployment-diagram.md)**
   - Shows the Clean Architecture layers (Presentation, Domain, Data)
   - Illustrates main packages within each layer
   - Demonstrates dependencies between layers and external services
   - Visualizes Dependency Injection structure

2. **[Use Case: Get Latest Movies](use-case-get-latest-movies.md)**
   - Flow for viewing the movie list
   - Interaction between UI, ViewModel, Use Case, and Repository
   - Caching strategy with Room database
   - API integration with TMDB service

3. **[Use Case: Get Movie Details](use-case-get-movie-details.md)**
   - Flow for viewing individual movie details
   - Navigation from list to details screen
   - Data transformation across architectural layers
   - Performance optimizations through caching

## Architecture Overview

The application follows **Clean Architecture** principles:

### Layers

- **Presentation Layer**: Jetpack Compose UI, ViewModels, Navigation
- **Domain Layer**: Business logic, Use Cases, Domain models
- **Data Layer**: Repositories, Data sources (API & Database), Data models

### Key Patterns

- **Unidirectional Data Flow (UDF)**: State flows from ViewModel to UI
- **Repository Pattern**: Single source of truth for data
- **Dependency Injection**: Hilt manages all dependencies
- **Result Wrappers**: Typed error handling with sealed classes

### External Dependencies

- **TMDB API**: RESTful API for movie data
- **Room Database**: Local SQLite for offline caching
- **Retrofit + OkHttp**: Networking stack
- **Coil**: Image loading

## Viewing Diagrams

All diagrams are created using [Mermaid](https://mermaid.js.org/), which is supported natively by:
- GitHub (renders automatically)
- Visual Studio Code (with Mermaid extension)
- IntelliJ IDEA (with Mermaid plugin)
- Most modern markdown viewers

## Related Documentation

- [Project README](../../README.md)
- [Development Guidelines](../../COPILOT.md)

---

**Last Updated**: 2025-10-25  
**Maintained By**: Pablo Perotti
