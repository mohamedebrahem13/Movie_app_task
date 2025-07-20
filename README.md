# Movie App

An Android application that displays a list of popular movies and allows users to search by text or voice. The app follows Clean Architecture principles and implements the MVI (Model-View-Intent) architectural pattern for clear state management and unidirectional data flow. Jetpack Compose is used for building the UI.

## Screenshots

<table style="width: 100%; border-collapse: collapse;"><tbody><tr><th style="width: 25%; text-align: center; border: 1px solid #ccc; padding: 8px;">Home screen </th><th style="width: 25%; text-align: center; border: 1px solid #ccc; padding: 8px;">Voice Permission </th><th style="width: 25%; text-align: center; border: 1px solid #ccc; padding: 8px;">Movie Details</th><th style="width: 25%; text-align: center; border: 1px solid #ccc; padding: 8px;">Search</th></tr><tr><td style="width: 25%; text-align: center; border: 1px solid #ccc; padding: 8px;"><img style="max-width: 100%; height: auto;" alt="Home screen" src="https://github.com/user-attachments/assets/03cf15b5-f305-452b-8187-79b2bdd78a77"></td><td style="width: 25%; text-align: center; border: 1px solid #ccc; padding: 8px;"><img style="max-width: 100%; height: auto;" alt="Voice Permission" src="https://github.com/user-attachments/assets/5774575c-fd25-4812-a590-13a087511060"></td><td style="width: 25%; text-align: center; border: 1px solid #ccc; padding: 8px;"><img style="max-width: 100%; height: auto;" alt="Movie Details" src="https://github.com/user-attachments/assets/f53e4ece-b1f9-4a07-abea-ee07ac02ae85"></td><td style="width: 25%; text-align: center; border: 1px solid #ccc; padding: 8px;"><img style="max-width: 100%; height: auto;" alt="Search" src="https://github.com/user-attachments/assets/72e5b67a-780d-43cc-942a-72555adddb81"></td></tr></tbody></table>

## 📽️ Preview

Click to watch short previews of the app in action:

- https://github.com/user-attachments/assets/cb1b99f2-70c2-43b6-ad8a-06cbe765ee69
  
- https://github.com/user-attachments/assets/a901c9d6-c9ff-4c34-9ac9-81aa933404ab
  
- https://github.com/user-attachments/assets/3a221dda-b5ea-44b6-82fb-d46df215a533

---
## Technologies Used

### Language
- **Kotlin** – programming language.

### Architecture & Patterns
- **Clean Architecture** – Separation of concerns between domain, data, and UI layers.
- MVI

###  UI
- **Jetpack Compose** – Modern declarative UI toolkit.
- ** New Navigation Compose** – Simplified navigation between composables.

### Networking
- **Retrofit** – Type-safe HTTP client for REST APIs.
- **Gson Converter** – JSON parsing.
- **OkHttp Logging Interceptor** – For debugging HTTP requests.
- adapter factory to map the retrofit exception to data exception internally
– Added an abstraction layer between Retrofit and potential alternatives (like Ktor) to allow easy swapping in the future.

###  Local Storage
- **Room** ksp version

### Dependency Injection
- **Dagger Hilt** – Dependency injection framework for Android.
- **KSP (Kotlin Symbol Processing)** – Annotation processing used by Hilt and Room.

### Testing
- **JUnit** – Unit testing framework.
- **MockK** – Mocking library for Kotlin.
- **Turbine** – Testing Kotlin Flow.
- **Truth** – Fluent assertions for tests.
- **kotlinx.coroutines.test** – For coroutine-based testing.

### Image Loading
- **Coil** – Lightweight image loading library built for Compose.

### Secrets Handling
- **Gradle buildConfig + secrets.properties** – For securely managing API keys.
- ⚠️ Note
This project will not run until you add a secrets.properties file in the app/ directory containing your API credentials



## Features

### Home Screen & Voice Search
- Search bar at the top with **text** and **voice** input.
- Integrated using `android.speech.SpeechRecognizer` to convert voice into text.
- Real-time search updates using the TMDb API.
- List of movies displayed based on the search query.

### Movie Details
- Tap on any movie to view comprehensive information, including:
  -  **Title** & **Original Title**
  -  **Poster Image**
  -  **Release Date**
  -  **Rating** & **Popularity Score**
  -  **Genres**
  -  **Overview / Description**
  -  **Adult Content Indicator**
  -  **Original Language**
  - …and more

### Offline Caching
- Uses **Room** to cache movies locally.
- Shows cached data on launch.
- Automatically fetches and updates data in the background.



## Project Structure

├── common  
│   ├── data  
│   │   └── repository  
│   │       ├── local  
│   │       │   └── db                        # Room database & DAOs  
│   │       ├── remote                        # Retrofit and call adapter factory  
│   │       └── utils  
│   ├── di  
│   │   ├── LocalModule                       # Provides Room & DAO  
│   │   └── NetworkModule                     # Provides Retrofit & interceptors  
│   ├── domain  
│   │   ├── repository  
│   │   │   └── remote  
│   │   │       └── INetworkProvide  
│   │   └── utils                             # Common utility classes  
│   └── ui  
│       └── BaseViewModel.kt                  # Base MVI ViewModel used across features  

├── feature  
│   ├── movie_list  
│   │   ├── data  
│   │   │   ├── mapper                        # DTO ↔ Entity ↔ Domain Model  
│   │   │   ├── models  
│   │   │   │   ├── dto  
│   │   │   │   └── entity  
│   │   │   └── repository  
│   │   │       ├── local  
│   │   │       │   └── MovieLocalDataSourceImpl.kt  
│   │   │       ├── remote  
│   │   │       │   └── MovieRemoteDataSourceImpl.kt  
│   │   │       └── MoviesRepositoryImpl.kt  
│   │   ├── domain  
│   │   │   ├── model  
│   │   │   │   └── Movie.kt  
│   │   │   ├── usecase  
│   │   │   │   ├── GetMoviesUseCase.kt  
│   │   │   │   ├── SearchMoviesByNameUseCase.kt  
│   │   │   │   ├── GetPopularMoviesLocalUseCase.kt  
│   │   │   │   ├── GetPopularMoviesRemoteUseCase.kt  
│   │   │   │   ├── SearchMoviesByNameLocalUseCase.kt  
│   │   │   │   ├── SearchMoviesByNameRemoteUseCase.kt  
│   │   │   │   └── ValidateSearchQueryUseCase.kt  
│   │   │   └── repository  
│   │   │       ├── local  
│   │   │       │   └── LocalDataSource.kt  
│   │   │       ├── remote  
│   │   │       │   └── RemoteDataSource.kt  
│   │   │       └── MoviesRepository.kt  
│   │   ├── di  
│   │   │   ├── MovieListModule.kt  
│   │   │   └── MovieUseCaseModule.kt  
│   │   └── ui  
│   │       ├── composable  
│   │       ├── viewmodel  
│   │       │   ├── MovieViewModel.kt  
│   │       │   └── MovieContract.kt          # Contains State, Intent, and Action  
│   │       └── screen  
│   │           └── HomeScreen.kt  

│   ├── movie_details  
│   │   ├── data  
│   │   │   ├── mapper  
│   │   │   └── repository  
│   │   │       ├── local  
│   │   │       │   └── MovieDetailsLocalDataSourceImpl.kt  
│   │   │       └── MovieDetailsRepositoryImpl.kt  
│   │   ├── domain  
│   │   │   ├── model  
│   │   │   │   └── MovieDetails.kt  
│   │   │   ├── usecase  
│   │   │   │   └── GetMovieDetailsByIdUseCase.kt  
│   │   │   └── repository  
│   │   │       ├── local  
│   │   │       │   └── MovieDetailsLocalDataSource.kt  
│   │   │       └── MovieDetailsRepository.kt  
│   │   ├── di  
│   │   │   └── MovieDetailsModule.kt  
│   │   └── ui  
│   │       ├── composable  
│   │       ├── viewmodel  
│   │       │   ├── MovieDetailsViewModel.kt  
│   │       │   └── MovieDetailsContract.kt   # State, Intent, Action  
│   │       └── screen  
│   │           └── MovieDetailsScreen.kt  
