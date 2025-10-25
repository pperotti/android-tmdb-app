# Development Instructions for TMDB Android App

## Project Overview
This is a modern Android application built with Jetpack Compose, following Clean Architecture principles and Google's latest architecture guidelines. The app displays movies from The Movie Database (TMDB) API.

## Architecture & Code Organization

### Clean Architecture Layers
The project follows a strict Clean Architecture structure with three main layers:

1. **Presentation Layer** (`presentation/`)
   - UI components using Jetpack Compose
   - ViewModels for state management
   - Navigation logic
   - UI models and state holders

2. **Domain Layer** (`domain/`)
   - Business logic and use cases
   - Domain models (entities)
   - Repository interfaces
   - Should NOT depend on Android framework or external libraries

3. **Data Layer** (`data/`)
   - Repository implementations
   - Data sources (remote API, local database)
   - Data models and mappers
   - Network and database configurations

### Package Structure
```
com.pperotti.android.moviescatalogapp/
├── di/                 # Application-level dependency injection
├── data/              # Data layer
│   ├── movie/        # Movie-specific data implementations
│   ├── common/       # Shared data components
│   └── di/           # Data layer DI modules
├── domain/           # Domain layer
│   ├── usecase/      # Business logic use cases
│   ├── common/       # Shared domain components
│   └── di/           # Domain layer DI modules
└── presentation/     # Presentation layer
    ├── ui/           # Compose UI components and screens
    ├── details/      # Movie details screen
    ├── navigation/   # Navigation graph
    └── di/           # Presentation layer DI modules
```

## Technology Stack & Libraries

### Core Technologies
- **Language**: 100% Kotlin (NO Java code should be added)
- **UI Framework**: Jetpack Compose
- **Minimum SDK**: 29 (Android 10)
- **Target SDK**: 35
- **JVM Target**: Java 11

### Key Libraries
- **Dependency Injection**: Hilt (Dagger)
- **Networking**: Retrofit + OkHttp
- **Image Loading**: Coil
- **Local Storage**: Room Database
- **Async Operations**: Kotlin Coroutines + Flow
- **State Management**: ViewModel + StateFlow/SharedFlow

### Code Quality Tools
- **ktlint**: Kotlin code formatter and linter
- **detekt**: Static code analysis tool

## Development Guidelines

### 1. Coding Standards

#### Kotlin Style
- Follow official Kotlin coding conventions
- Use ktlint for automatic formatting
- Avoid nullable types when possible; use sealed classes for result states
- Prefer `val` over `var`
- Use trailing commas in multi-line declarations

#### Naming Conventions
- **Packages**: lowercase, no underscores (e.g., `moviedetails`)
- **Classes**: PascalCase (e.g., `MovieRepository`)
- **Functions**: camelCase (e.g., `getMovieDetails()`)
- **Variables**: camelCase (e.g., `movieId`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_RETRY_ATTEMPTS`)
- **Composables**: PascalCase (e.g., `MovieListScreen()`)
- **Resources**: snake_case with prefix (e.g., `ic_movie_placeholder`, `txt_error_message`)

#### File Naming
- **ViewModels**: `*ViewModel.kt` (e.g., `MovieDetailsViewModel.kt`)
- **Repositories**: `*Repository.kt` and `*RepositoryImpl.kt`
- **Use Cases**: `*UseCase.kt` (e.g., `GetMovieDetailsUseCase.kt`)
- **Screens**: `*Screen.kt` (e.g., `MovieListScreen.kt`)
- **Data Models**: `*Entity.kt` for domain, `*Dto.kt` for API, `*Model.kt` for UI

### 2. Architecture Patterns

#### Unidirectional Data Flow (UDF)
- ViewModels expose StateFlow/SharedFlow for UI state
- UI sends user actions to ViewModel
- ViewModel processes actions and updates state
- UI reacts to state changes

#### Dependency Rule
- Inner layers should NOT know about outer layers
- Dependencies point inward: Presentation → Domain ← Data
- Domain layer is the core and has no external dependencies

#### Repository Pattern
- Repositories are the single source of truth
- Handle data from multiple sources (remote API + local DB)
- Map between data models and domain models

### 3. API Configuration

#### Required Setup
Add these properties to `local.properties` (NEVER commit this file):
```properties
API_BASE_URL=https://api.themoviedb.org/3/
SERVICE_TOKEN=<your_tmdb_api_token>
```

#### API Key Security
- API keys must ONLY be stored in `local.properties`
- Use BuildConfig for accessing keys in code
- Add `local.properties` to `.gitignore`
- Never hardcode API keys in source code

### 4. Dependency Injection

#### Hilt Usage
- Use `@HiltAndroidApp` for Application class
- Use `@AndroidEntryPoint` for Activities and Composables
- Create separate DI modules per layer:
  - `DataModule` for repositories and data sources
  - `DomainModule` for use cases
  - `NetworkModule` for Retrofit, OkHttp
  - `DatabaseModule` for Room

#### Module Organization
- Keep modules focused and single-purpose
- Use `@Singleton` for app-level dependencies
- Use `@ViewModelScoped` for ViewModel dependencies

### 5. Compose UI Guidelines

#### Composable Best Practices
- Keep composables small and focused
- Separate stateful and stateless composables
- Use `remember` and `rememberSaveable` appropriately
- Hoist state to the appropriate level
- Use `LaunchedEffect` for side effects

#### State Management
- Use `State` for local UI state
- Use `ViewModel` + `StateFlow` for screen-level state
- Avoid passing ViewModel directly to child composables
- Pass only necessary data and callbacks

#### Previews
- Add `@Preview` annotations for all composables
- Create preview functions with sample data
- Support both light and dark themes in previews

### 6. Testing Requirements

#### Unit Tests
- Write unit tests for ViewModels, Use Cases, and Repositories
- Use JUnit 4 for test framework
- Mock external dependencies (Retrofit, Room)
- Aim for >80% code coverage in business logic

#### UI Tests
- Use Compose Testing for UI tests
- Test user interactions and navigation
- Verify state changes and UI updates

### 7. Localization

#### Internationalization Support
- The app currently supports English and Spanish
- All user-facing strings must be in `strings.xml`
- Never hardcode strings in code or composables
- Use `stringResource(R.string.*)` in Composables

#### Adding New Languages
1. Create new values folder: `values-{language_code}/`
2. Add translated `strings.xml`
3. Test on device with that locale

### 8. Orientation Support

#### Responsive Design
- All screens must support both portrait and landscape
- Use different layouts when appropriate
- Test all screens in both orientations
- Use `WindowSizeClass` for adaptive layouts

### 9. Error Handling

#### Network Errors
- Use sealed classes for result states (Success, Error, Loading)
- Provide user-friendly error messages
- Implement retry mechanisms
- Log errors appropriately

#### Example Pattern
```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
```

### 10. Git Workflow & Pull Request Guidelines

#### Commit Messages
Follow the **Conventional Commits** specification:

**Format**: `type(scope): description`

**Types**:
- `feat`: New feature for the user
- `fix`: Bug fix for the user
- `docs`: Documentation changes only
- `style`: Code style changes (formatting, missing semicolons, etc.)
- `refactor`: Code change that neither fixes a bug nor adds a feature
- `test`: Adding or updating tests
- `chore`: Changes to build process or auxiliary tools
- `perf`: Performance improvements
- `ci`: CI/CD configuration changes

**Examples**:
```
feat(movies): add search functionality
fix(details): correct image loading issue
docs(readme): update installation instructions
refactor(repository): simplify data mapping logic
test(viewmodel): add unit tests for MovieDetailsViewModel
```

**Commit Message Rules**:
- Use imperative mood ("add" not "added" or "adds")
- No period at the end
- Keep the subject line under 72 characters
- Add a body if needed to explain the "why" behind changes
- Reference issue numbers when applicable: `fix(movies): resolve crash on empty list (#123)`

#### Before Committing - Checklist

**MANDATORY** steps before every commit:

1. **Format Code**
   ```bash
   ./gradlew ktlintFormat
   ```

2. **Run Static Analysis**
   ```bash
   ./gradlew detekt
   ```
   - Fix all errors
   - Address warnings when reasonable

3. **Run Tests**
   ```bash
   ./gradlew test
   ```
   - All tests must pass
   - Add new tests for new functionality

4. **Review Your Changes**
   ```bash
   git diff
   ```
   - Remove debug code, console logs, and commented code
   - Verify no sensitive data is included
   - Check that only relevant changes are staged

5. **Build Verification**
   ```bash
   ./gradlew assembleDebug
   ```
   - Ensure the app builds successfully

#### Branch Strategy
- `master`: production-ready code (protected)
- `develop`: integration branch for ongoing development
- `feature/*`: new features (e.g., `feature/search-movies`)
- `bugfix/*`: bug fixes (e.g., `bugfix/image-loading-crash`)
- `hotfix/*`: urgent production fixes (e.g., `hotfix/api-token-expiry`)
- `refactor/*`: code refactoring (e.g., `refactor/repository-layer`)

#### Branch Naming Enforcement

**IMPORTANT**: Branches that do not follow the naming conventions will be rejected during push.

The repository is configured with a pre-push hook that validates branch names before allowing a push to the remote repository. This ensures all branches follow the established naming strategy.

**Allowed Branch Patterns**:
- `master` - main production branch
- `develop` - integration branch
- `feature/*` - feature branches (e.g., `feature/movie-search`)
- `bugfix/*` - bug fix branches (e.g., `bugfix/crash-on-empty-list`)
- `hotfix/*` - urgent fix branches (e.g., `hotfix/security-patch`)
- `refactor/*` - refactoring branches (e.g., `refactor/data-layer`)
- `docs/*` - documentation branches (e.g., `docs/update-readme`)
- `test/*` - test-related branches (e.g., `test/integration-tests`)
- `chore/*` - maintenance branches (e.g., `chore/update-dependencies`)

**What Happens on Invalid Branch Names**:
If you attempt to push a branch with an invalid name, the push will be rejected with an error message indicating:
- The branch name does not follow conventions
- Which naming patterns are allowed
- Examples of valid branch names

**To Fix an Invalid Branch Name**:
```bash
# Rename your current branch
git branch -m old-branch-name new-valid-branch-name

# If already pushed, delete the remote branch and push with new name
git push origin --delete old-branch-name
git push -u origin new-valid-branch-name
```

#### Creating Pull Requests

**PR Title Format**:
Follow the same conventional commit format:
```
feat(movies): add search functionality
fix(details): resolve image loading crash
```

**PR Description Template**:
```markdown
## Description
Brief description of what this PR does and why.

## Type of Change
- [ ] 🎉 New feature (non-breaking change which adds functionality)
- [ ] 🐛 Bug fix (non-breaking change which fixes an issue)
- [ ] 💥 Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] 📝 Documentation update
- [ ] ♻️ Code refactoring
- [ ] ✅ Test updates

## Related Issues
Closes #(issue number)
Related to #(issue number)

## Changes Made
- Change 1
- Change 2
- Change 3

## Testing Done
- [ ] Unit tests added/updated
- [ ] UI tests added/updated
- [ ] Manual testing completed
- [ ] Tested on multiple screen sizes
- [ ] Tested in both portrait and landscape orientations
- [ ] Tested with different locales (English/Spanish)

## Screenshots (if applicable)
[Add screenshots or screen recordings for UI changes]

## Pre-Submission Checklist
- [ ] Code formatted with ktlint (`./gradlew ktlintFormat`)
- [ ] Static analysis passed (`./gradlew detekt`)
- [ ] All tests pass (`./gradlew test`)
- [ ] App builds successfully (`./gradlew assembleDebug`)
- [ ] No sensitive data (API keys, tokens) included
- [ ] Documentation updated (if needed)
- [ ] Follows Clean Architecture principles
- [ ] No deprecated APIs used
- [ ] All strings externalized to `strings.xml`

## Reviewer Notes
[Any specific areas you'd like reviewers to focus on]
```

#### PR Best Practices

1. **Keep PRs Small and Focused**
   - One feature or fix per PR
   - Aim for <400 lines of code changed
   - Split large features into multiple PRs

2. **Self-Review First**
   - Review your own PR before requesting reviews
   - Check the "Files changed" tab on GitHub
   - Add comments to explain complex logic

3. **Write Clear Descriptions**
   - Explain the "why" not just the "what"
   - Include before/after comparisons for UI changes
   - Link to relevant documentation or design specs

4. **Request Reviews Appropriately**
   - Tag specific reviewers when needed
   - Respond to review comments promptly
   - Mark conversations as resolved when addressed

5. **Keep PR Updated**
   - Rebase on latest `develop` before requesting review
   - Resolve merge conflicts promptly
   - Re-run checks after addressing feedback

#### Code Review Guidelines

**As a Reviewer**:
- Verify all checklist items are completed
- Check adherence to Clean Architecture
- Ensure proper error handling
- Validate test coverage
- Review performance implications
- Check for accessibility considerations
- Approve only when all concerns are addressed

**As a PR Author**:
- Address all review comments
- Don't take feedback personally
- Ask for clarification when needed
- Update the PR based on feedback
- Thank reviewers for their time

#### Merging Strategy

- **Squash and Merge**: For feature branches (keeps history clean)
- **Merge Commit**: For release branches (preserves full history)
- **Rebase and Merge**: For small, atomic commits (linear history)

**After Merge**:
1. Delete the feature branch
2. Verify the change in `develop` or `master`
3. Monitor for any issues
4. Update related documentation if needed

## Common Tasks

### Building the App
```bash
./gradlew assembleDebug
./gradlew assembleRelease
```

### Running Code Quality Checks
```bash
# Format code with ktlint
./gradlew ktlintFormat

# Check code style
./gradlew ktlintCheck

# Run static analysis
./gradlew detekt

# Run all checks
./gradlew check
```

### Running Tests
```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest

# All tests
./gradlew testDebugUnitTest
```

### Adding New Dependencies
1. Add dependency to `libs.versions.toml` in `gradle/` folder
2. Reference in `build.gradle.kts` using `libs.*`
3. Sync project with Gradle files
4. Document why the dependency is needed

## What NOT to Do

### ❌ Prohibited Actions
- Never add Java code (Kotlin only)
- Never commit API keys or secrets
- Never use deprecated Android APIs
- Never bypass Hilt for dependency injection
- Never put business logic in Activities/Composables
- Never use `!!` (double bang operator) without careful consideration
- Never ignore ktlint or detekt warnings without good reason
- Never hardcode strings, colors, or dimensions
- Never make network calls on the main thread

## AI Assistant Guidelines

When assisting with this project:
1. Always follow Clean Architecture principles
2. Maintain the existing package structure
3. Use Hilt for all dependency injection
4. Write code in Kotlin, never Java
5. Keep composables small and reusable
6. Add proper error handling
7. Include unit tests for new features
8. Update documentation if needed
9. Follow existing naming conventions
10. Run code quality checks before finishing

## Additional Resources

- [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- [Android Architecture Guide](https://developer.android.com/topic/architecture)
- [Jetpack Compose Guidelines](https://developer.android.com/jetpack/compose)
- [TMDB API Documentation](https://developers.themoviedb.org/3)

---

**Last Updated**: 2025-10-25  
**Maintained By**: Pablo Perotti
