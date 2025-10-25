# Deployment Diagram

This UML 2.5 deployment diagram shows the Clean Architecture layers and component distribution within the TMDB Android Application.

```mermaid
graph TB
    subgraph Device["<<device>><br/>Android Device"]
        subgraph AppRuntime["<<execution environment>><br/>Android Runtime (ART)"]
            subgraph PresentationNode["<<node>><br/>Presentation Layer"]
                MainComp["<<component>><br/>presentation.main<br/>---<br/>MainScreen<br/>MainViewModel"]
                DetailsComp["<<component>><br/>presentation.details<br/>---<br/>DetailsScreen<br/>DetailsViewModel"]
                NavComp["<<component>><br/>presentation.navigation<br/>---<br/>Navigation Graph"]
                UIComp["<<component>><br/>presentation.ui<br/>---<br/>Compose Components<br/>Theme"]
            end
            
            subgraph DomainNode["<<node>><br/>Domain Layer"]
                UseCaseComp["<<component>><br/>domain.usecase<br/>---<br/>GetLatestMovies<br/>GetMovieDetails"]
                DomainCommonComp["<<component>><br/>domain.common<br/>---<br/>DomainResult<br/>Domain Models"]
            end
            
            subgraph DataNode["<<node>><br/>Data Layer"]
                RepoComp["<<component>><br/>data.movie<br/>---<br/>MovieRepository<br/>RemoteDataSource<br/>LocalDataSource"]
                DataCommonComp["<<component>><br/>data.common<br/>---<br/>DataResult<br/>DTOs"]
            end
            
            subgraph DINode["<<node>><br/>Dependency Injection"]
                HiltComp["<<component>><br/>di<br/>---<br/>Hilt Modules<br/>@HiltAndroidApp"]
            end
            
            subgraph StorageNode["<<node>><br/>Local Storage"]
                RoomDB[("<<artifact>><br/>Room Database<br/>---<br/>SQLite")]
            end
        end
    end
    
    subgraph Cloud["<<cloud>><br/>Internet"]
        TMDBAPI["<<component>><br/>TMDB API<br/>---<br/>api.themoviedb.org<br/>REST API"]
    end
    
    MainComp -->|uses| UseCaseComp
    DetailsComp -->|uses| UseCaseComp
    NavComp -->|controls| MainComp
    NavComp -->|controls| DetailsComp
    
    UseCaseComp -->|uses| RepoComp
    UseCaseComp -.->|depends on| DomainCommonComp
    
    RepoComp -->|HTTP/HTTPS| TMDBAPI
    RepoComp -->|JDBC| RoomDB
    RepoComp -.->|depends on| DataCommonComp
    
    HiltComp -.->|provides| MainComp
    HiltComp -.->|provides| DetailsComp
    HiltComp -.->|provides| UseCaseComp
    HiltComp -.->|provides| RepoComp
    
    classDef deviceStyle fill:#f5f5f5,stroke:#333,stroke-width:3px
    classDef presentationStyle fill:#e1f5ff,stroke:#01579b,stroke-width:2px
    classDef domainStyle fill:#fff9c4,stroke:#f57f17,stroke-width:2px
    classDef dataStyle fill:#f3e5f5,stroke:#4a148c,stroke-width:2px
    classDef infrastructureStyle fill:#e8f5e9,stroke:#1b5e20,stroke-width:2px
    classDef externalStyle fill:#ffebee,stroke:#b71c1c,stroke-width:2px
    classDef storageStyle fill:#fff3e0,stroke:#e65100,stroke-width:2px

    class Device,AppRuntime deviceStyle
    class PresentationNode,MainComp,DetailsComp,NavComp,UIComp presentationStyle
    class DomainNode,UseCaseComp,DomainCommonComp domainStyle
    class DataNode,RepoComp,DataCommonComp dataStyle
    class DINode,HiltComp infrastructureStyle
    class Cloud,TMDBAPI externalStyle
    class StorageNode,RoomDB storageStyle
```

## UML 2.5 Compliance

This diagram follows UML 2.5 deployment diagram specifications:

### Stereotypes Used
- **`<<device>>`**: Physical computing resource (Android Device)
- **`<<execution environment>>`**: Software platform (Android Runtime)
- **`<<node>>`**: Computational resource (architectural layers)
- **`<<component>>`**: Modular software units (packages and modules)
- **`<<artifact>>`**: Physical file or database (SQLite database)
- **`<<cloud>>`**: Internet-based services

### Relationships
- **Solid arrows**: Dependencies and communication paths
- **Dashed arrows**: Deployment/provision relationships
- **Labels**: Communication protocols (HTTP/HTTPS, JDBC)

## Architecture Layers

### Device Layer
- **Android Device**: Physical mobile device running the application
- **Android Runtime (ART)**: Execution environment for the app

### Application Layers

#### Presentation Layer (Node)
Components handling UI and user interaction:
- **presentation.main**: Main screen with movie list
- **presentation.details**: Movie details screen
- **presentation.navigation**: Navigation graph and routing
- **presentation.ui**: Reusable Compose components and theme

#### Domain Layer (Node)
Business logic components:
- **domain.usecase**: Business rules implementation
  - `GetLatestMovies`: Retrieves paginated movie lists
  - `GetMovieDetails`: Retrieves specific movie details
- **domain.common**: Domain models and result wrappers

#### Data Layer (Node)
Data access and management components:
- **data.movie**: Repository and data source implementations
  - `MovieRepository`: Single source of truth
  - `RemoteDataSource`: API communication
  - `LocalDataSource`: Database operations
- **data.common**: Data transfer objects (DTOs) and result types

#### Dependency Injection (Node)
Infrastructure for dependency management:
- **di**: Hilt configuration and modules
- Provides instances across all layers

### External Systems

#### Local Storage
- **Room Database**: SQLite-based local persistence
- Connected via JDBC protocol
- Stores cached movie data

#### Cloud Services
- **TMDB API**: RESTful web service
- Accessible via HTTP/HTTPS
- Provides movie data and images

## Communication Protocols

- **HTTP/HTTPS**: Secure communication with TMDB API
- **JDBC**: Database access protocol for Room
- **Dependency Injection**: Hilt provides component instances

## Deployment Characteristics

- **Single Device Deployment**: All application components run on the Android device
- **Client-Server Architecture**: App acts as client to TMDB API
- **Local Caching**: Room database enables offline functionality
- **Layered Architecture**: Clean separation of concerns across layers
