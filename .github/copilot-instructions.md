# Copilot instructions for TMDBApp

This file helps future Copilot sessions understand how to build, test, lint, and navigate the repository.

---

## Build / test / lint (commands)
- Use the Gradle wrapper from the repo root: `./gradlew <task>`
- Full build: `./gradlew build`
- Assemble debug APK: `./gradlew assembleDebug`
- Run unit tests (all): `./gradlew :app:testDebugUnitTest`
- Run a single unit test (class or pattern):
  - Example (full class path):
    `./gradlew :app:testDebugUnitTest --tests "com.pperotti.android.moviescatalogapp.presentation.common.RevenueFormatterTest"`
  - Wildcard example: `./gradlew :app:testDebugUnitTest --tests "*RevenueFormatterTest"`
- Run instrumentation tests (device/emulator):
  - Full instrumented suite: `./gradlew :app:connectedDebugAndroidTest` (or use Android Studio to run a single test/device test)
- Lint / static analysis:
  - Android lint: `./gradlew :app:lint`
  - ktlint checks: `./gradlew ktlintCheck`
  - ktlint autoformat: `./gradlew ktlintFormat`
  - detekt: `./gradlew detekt`

Notes: tasks may be available at project or module level (e.g., `:app:detekt`). Use the Gradle wrapper so CI/locally consistent tool versions are used.

---

## Quick environment pointers
- Java / toolchain: project config targets Java 17 (Kotlin jvmToolchain configured to 17). Use JDK 17 for local builds if not relying on Gradle toolchains.
- API credentials: add to `local.properties` (NOT checked in):
  ```text
  API_BASE_URL=https://api.themoviedb.org/3/
  SERVICE_TOKEN=<your TMDB token>
  ```
  These are injected into BuildConfig and consumed by the app (see `app/build.gradle.kts` buildConfigField lines).
- Version catalog: dependencies and plugin versions are defined in `gradle/libs.versions.toml`.

---

## High-level architecture (big picture)
- Clean Architecture with three main layers:
  - Presentation: Jetpack Compose UI, ViewModels, navigation (package: `presentation`)
  - Domain: use cases and domain models (package: `domain`)
  - Data: repositories, remote (Retrofit) and local (Room) data sources (package: `data`)
- Key flow: UI -> ViewModel -> UseCase -> Repository -> DataSource (remote/db). UDF (unidirectional data flow) is used: ViewModel exposes state to Compose UI.
- DI: Hilt is used across layers. DI modules live under `di` and in each layer (e.g., `data/di/DataModule.kt`, `domain/di/DomainModule.kt`, `presentation/di/PresentationModule.kt`).
- Networking/caching: Retrofit + OkHttp for API; Room for offline cache and `TypeConverters` for stored entities.
- Error/result handling: typed result wrappers (e.g., `DataResult`, `DomainResult`) and sealed classes for explicit flows between layers.

---

## Key repository-specific conventions
- Kotlin-only project (no new Java). Use idiomatic Kotlin + Compose.
- Package layout mirrors Clean Architecture: `data`, `domain`, `presentation` subpackages. Prefer placing classes in those packages by responsibility rather than file grouping.
- Naming:
  - ViewModels end with `ViewModel` and provide `*UiState` data classes for Compose screens.
  - UseCase classes are named like `GetLatestMovies`, `GetMovieDetails` and placed in `domain/usecase`.
  - Repository interfaces/implementations live under `data/*` and use `MovieRepository` naming.
- DI modules: register bindings with Hilt modules (singletons where appropriate); prefer `@Provides`/`@Binds` patterns already present in `di` modules.
- Tests:
  - Unit tests live in `app/src/test/java/...`
  - Instrumented tests live in `app/src/androidTest/java/...` and rely on Android Test artifacts declared in the Gradle config.
- Resources:
  - Strings (EN/ES) in `res/values` and `res/values-es` for Spanish translations.
- Code style: ktlint + detekt enforced. Run `ktlintCheck` before CI/PRs and fix style with `ktlintFormat` when needed.

---

## Useful file entry points for code navigation
- App module: `app/build.gradle.kts` (build flags, BuildConfig injection)
- DI modules: `app/src/main/java/.../di/*` and the top-level `di/` files (DataModule, DomainModule, PresentationModule)
- Network client & API: `TmdbService.kt`, `TmdbApi.kt`, `MovieRemoteDataSource.kt`
- Local cache: `MovieLocalDataSource.kt`, `StorageEntities.kt`, `MovieTypeConverters.kt`
- Presentation: `MainScreen.kt`, `DetailsScreen.kt`, `MainViewModel.kt`, `DetailsViewModel.kt`
- Tests: `app/src/test/...` and `app/src/androidTest/...`

---

If this repo grows new tools/configs, merge any important instructions into this file (e.g., CI tasks, emulator setup, or commands needed for new static analysis tools).

Last updated: 2026-06-13
Maintainer: Pablo Perotti
