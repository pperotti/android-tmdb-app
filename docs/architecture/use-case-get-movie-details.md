# Use Case: Get Movie Details

This diagram illustrates the flow when a user selects a movie to view its details.

```mermaid
graph TB
    User([User])
    MainScreen[MainScreen<br/>Movie List]
    DetailsScreen[DetailsScreen<br/>Composable]
    DetailsViewModel[DetailsViewModel]
    GetMovieDetails[GetMovieDetails<br/>Use Case]
    MovieRepository[MovieRepository]
    RemoteDS[RemoteDataSource<br/>TMDB API]
    LocalDS[LocalDataSource<br/>Room Database]
    
    User -->|Selects Movie| MainScreen
    MainScreen -->|Navigate with<br/>Movie ID| Navigation[Navigation]
    Navigation -->|Route to Details| DetailsScreen
    DetailsScreen -->|Observes State| DetailsViewModel
    DetailsViewModel -->|Calls with ID| GetMovieDetails
    GetMovieDetails -->|Requests Details| MovieRepository
    
    MovieRepository -->|Check Cache| LocalDS
    
    LocalDS -->|Cache Miss| MovieRepository
    MovieRepository -->|Fetch from API| RemoteDS
    RemoteDS -->|HTTP Request<br/>GET /movie/{id}| API[TMDB API]
    API -->|Movie Details JSON| RemoteDS
    RemoteDS -->|DataResult| MovieRepository
    
    MovieRepository -->|Save to Cache| LocalDS
    MovieRepository -->|Return Data| GetMovieDetails
    
    GetMovieDetails -->|Transform to<br/>Domain Model| GetMovieDetails
    GetMovieDetails -->|DomainResult| DetailsViewModel
    DetailsViewModel -->|Update State| DetailsViewModel
    DetailsViewModel -->|Emit State| DetailsScreen
    DetailsScreen -->|Display Details| User

    classDef userStyle fill:#fff,stroke:#333,stroke-width:2px
    classDef uiStyle fill:#e1f5ff,stroke:#01579b,stroke-width:2px
    classDef domainStyle fill:#fff9c4,stroke:#f57f17,stroke-width:2px
    classDef dataStyle fill:#f3e5f5,stroke:#4a148c,stroke-width:2px
    classDef externalStyle fill:#ffebee,stroke:#b71c1c,stroke-width:2px

    class User userStyle
    class MainScreen,DetailsScreen,DetailsViewModel,Navigation uiStyle
    class GetMovieDetails domainStyle
    class MovieRepository,RemoteDS,LocalDS dataStyle
    class API externalStyle
```

## Flow Description

### Actor
- **User**: End user browsing movies

### Preconditions
- User is viewing the movie list screen
- Network connectivity is available
- Movie ID is valid

### Main Flow

1. **User selects movie**: User taps on a movie card in the list
2. **Navigation triggered**: MainScreen passes movie ID to navigation system
3. **DetailsScreen opens**: Navigation routes to DetailsScreen with movie ID parameter
4. **ViewModel requests details**: DetailsViewModel calls GetMovieDetails use case with the ID
5. **Use case delegates to repository**: GetMovieDetails invokes MovieRepository
6. **Repository checks cache**: LocalDataSource is queried for cached movie details
7. **Fetch from API (if needed)**:
   - If details not in cache
   - RemoteDataSource makes HTTP GET request to `/movie/{id}`
8. **API responds**: Returns JSON with comprehensive movie details including:
   - Title, overview, poster
   - Genres, revenue, status
   - Vote average and count
   - IMDB ID and homepage
9. **Data transformation**:
   - RemoteDataSource transforms JSON to DataMovieDetails
   - Repository saves to local cache
   - Use case transforms to DomainMovieDetails with full image URLs
10. **State update**: DetailsViewModel updates UI state
11. **UI renders**: DetailsScreen displays movie information

### Postconditions
- Movie details are displayed on screen
- Details are cached for future quick access
- User can navigate back to list

### Alternative Flows

#### Cached Data Available
- Details loaded immediately from local database
- No API call required
- Faster user experience

#### Network Error
- Error state displayed
- Option to retry
- User can navigate back

#### Invalid Movie ID
- Error message displayed
- User redirected to movie list

### Data Displayed

The details screen shows:
- **Header**: Movie title and poster image
- **Metadata**: Release status, revenue
- **Rating**: Vote average and count
- **Genres**: List of movie genres
- **Synopsis**: Full movie overview
- **Links**: IMDB link, official homepage

### Key Components

- **DetailsScreen**: Jetpack Compose UI with scrollable content
- **DetailsViewModel**: Manages movie details state
- **GetMovieDetails**: Business logic for retrieving specific movie
- **MovieRepository**: Single source of truth for movie data
- **RemoteDataSource**: API communication layer
- **LocalDataSource**: Database persistence layer

### Performance Considerations

- **Caching**: Details are cached to reduce API calls
- **Image Loading**: Poster images loaded asynchronously with Coil
- **State Management**: Loading/Success/Error states managed via StateFlow
