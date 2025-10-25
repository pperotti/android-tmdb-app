# Deployment Diagram

This diagram shows the Clean Architecture layers and main packages within the TMDB Android Application.

```mermaid
graph TB
    subgraph "Presentation Layer"
        P1[presentation.main<br/>MainScreen]
        P2[presentation.details<br/>DetailsScreen]
        P3[presentation.navigation<br/>Navigation Graph]
        P4[presentation.ui<br/>UI Components & Theme]
        P5[presentation.common<br/>Common UI Utils]
        P6[presentation.di<br/>Presentation DI Modules]
    end

    subgraph "Domain Layer"
        D1[domain.usecase<br/>GetLatestMovies<br/>GetMovieDetails]
        D2[domain.common<br/>DomainResult<br/>Domain Models]
        D3[domain.di<br/>Domain DI Modules]
    end

    subgraph "Data Layer"
        DA1[data.movie<br/>MovieRepository<br/>RemoteDataSource<br/>LocalDataSource]
        DA2[data.common<br/>DataResult<br/>Data Models]
        DA3[data.di<br/>Data DI Modules<br/>Network Module<br/>Database Module]
    end

    subgraph "External Services"
        API[TMDB API<br/>api.themoviedb.org]
        DB[(Room Database<br/>Local Storage)]
    end

    subgraph "Dependency Injection"
        DI[di<br/>Application DI<br/>Hilt Configuration]
    end

    P1 --> D1
    P2 --> D1
    P3 --> P1
    P3 --> P2
    
    D1 --> DA1
    D2 -.-> D1
    
    DA1 --> API
    DA1 --> DB
    DA2 -.-> DA1
    
    DI --> P6
    DI --> D3
    DI --> DA3
    
    P6 -.-> P1
    P6 -.-> P2
    D3 -.-> D1
    DA3 -.-> DA1

    classDef presentationStyle fill:#e1f5ff,stroke:#01579b,stroke-width:2px
    classDef domainStyle fill:#fff9c4,stroke:#f57f17,stroke-width:2px
    classDef dataStyle fill:#f3e5f5,stroke:#4a148c,stroke-width:2px
    classDef externalStyle fill:#ffebee,stroke:#b71c1c,stroke-width:2px
    classDef diStyle fill:#e8f5e9,stroke:#1b5e20,stroke-width:2px

    class P1,P2,P3,P4,P5,P6 presentationStyle
    class D1,D2,D3 domainStyle
    class DA1,DA2,DA3 dataStyle
    class API,DB externalStyle
    class DI diStyle
```

## Layer Descriptions

### Presentation Layer
- **Main Package**: UI screens and ViewModels for displaying movie lists
- **Details Package**: UI screens and ViewModels for movie details
- **Navigation**: Navigation graph and routing logic
- **UI Package**: Reusable Compose components and theme configuration
- **Common**: Shared presentation utilities
- **DI**: Hilt modules for presentation layer dependencies

### Domain Layer
- **Use Case Package**: Business logic interfaces and implementations
  - `GetLatestMovies`: Retrieves paginated list of latest movies
  - `GetMovieDetails`: Retrieves detailed information for a specific movie
- **Common**: Domain models and result wrappers
- **DI**: Hilt modules for domain layer dependencies

### Data Layer
- **Movie Package**: Repository and data source implementations
  - `MovieRepository`: Single source of truth for movie data
  - Remote and local data sources
- **Common**: Data models (DTOs) and result wrappers
- **DI**: Network configuration (Retrofit, OkHttp) and database setup (Room)

### External Dependencies
- **TMDB API**: RESTful API providing movie data
- **Room Database**: Local SQLite database for caching

### Dependency Injection
- **Application DI**: Hilt configuration at application level
- Coordinates dependency provision across all layers
