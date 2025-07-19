# The MovieDB Android Application (TMDBApp)

## Table of Contents

- [The MovieDB Android Application (TMDBApp)](#the-moviedb-android-application-tmdbapp)
  - [Table of Contents](#table-of-contents)
  - [Introduction](#introduction)
  - [API Key](#api-key)
  - [Features](#features)
  - [Key Technical Decisions](#key-technical-decisions)
    - [Libraries](#libraries)
    - [The project support two scenarios](#the-project-support-two-scenarios)
  - [Screenshots](#screenshots)
  - [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
  - [Usage](#usage)
  - [License](#license)
  - [Acknowledgments](#acknowledgments)

## Introduction

The application created in this repo is a demo application which intention is to reflect
how a modern application should be structured.

## API Key

IMPORTANT! In order to make this work, you should the following properties to your local.properties file:
```text
API_BASE_URL=https://api.themoviedb.org/3/
SERVICE_TOKEN=<your TMDB token>
```

## Features

The application allows the user:
* To see a list of latest movies as defined by the TMDBApi.
* To see the details of such movies when each of them are selected.
* Display the data in a way that can be read correctly depending on the orientation.
* Display the labels used in the application in English or Spanish.

## Key Technical Decisions

### Libraries
The project is written 100% in Kotlin since this is the actual standard. 

No Java code should be used in new features in modern application written from scratch.
 
* Jetpack Compose
* Kotlin
* Code organized following latest Google's Architecture Guideline
* Unidirectional Data Flow
* Dependency Injection
* Storage Support using Room

### The project support two scenarios

* List A page of latest movies
* Get the details of a movie

## Screenshots
Here are some screenshots of the app.

<table>
  <tr>
    <td colspan="2" style="text-align:center;">
        <img src="documentation/images/TMDB_Logo.png" alt="Logo" style="width:20%;height:20%">
    </td>
  </tr>
  <tr>
    <td style="text-align:center;">
      <img src="documentation/images/latest_movies.png" alt="Main Screen showing a list of movies" style="width:50%;height:50%;">
    </td>    
    <td>
      <img src="documentation/images/latest_movies_landscape.png" alt="Main Screen showing a list of movies" style="width:80%;height:80%;">      
    </td>    
  </tr>
  <tr>
    <td style="text-align:center;">
      <img src="documentation/images/movie_details.png" alt="Screen showing the list of a movie." style="width:50%;height:50%;">
    </td>    
    <td>
      <img src="documentation/images/movie_details_landscape.png" alt="Screen showing the list of a movie." style="width:80%;height:80%;">    
    </td>    
  </tr>
</table>

## Getting Started

### Prerequisites

List any prerequisites for running the project. This could include Android Studio, specific SDK
versions, etc.

- Java 1.8 or higher
- Android Studio 2024 or superior
- Android SDK version 29 & 35

### Installation

Provide step-by-step instructions on how to set up and run your project.

1. Clone the repository:
   ```sh
   git clone git@github.com:pperotti/android-tmdb-app.git
   ```
2. Open the project in Android Studio.
3. Build and run the application on an emulator or connected device.

## Usage

All you have to do is selecting the app's icon and wait for the app to load the list of most recent movies available.

Once the list is displayed, you can select an individual movie to see its details.

All screens support a personalized displayed depending on the device's orientation.

## License

This project is licensed under the [MIT License](https://choosealicense.com/licenses/mit/) - see the
[LICENSE.md](https://github.com/username/repository/blob/master/LICENSE.md) file for details.

## Acknowledgments

All the project created here was done by Pablo Perotti

- [github.com/pperotti] (GitHub)

```