# Weather App

A small, single-screen Android weather app built with modern Android
development practices: Jetpack Compose, MVVM, Hilt, Retrofit/Kotlin
serialization, Coroutines/Flow, and DataStore — all wired together with a
clean data / domain / presentation architecture.

Search any city, see its current conditions, and the app remembers the last
city you looked up so it's there when you reopen it.

## Why this API

The app uses **[Open-Meteo](https://open-meteo.com/)**, a free weather API
that requires **no API key and no sign-up**, so the project builds and runs
immediately with zero configuration:

- `https://geocoding-api.open-meteo.com/v1/search` — turns a city name (e.g.
  `"Nairobi"`) into latitude/longitude.
- `https://api.open-meteo.com/v1/forecast` — returns current temperature,
  "feels like" temperature, humidity, wind speed, and a WMO weather code for
  those coordinates.

## Architecture

```
presentation layer  →  domain layer  →  data layer
   (Compose UI,          (models,         (Retrofit services,
    ViewModel,            repository       repository impl,
    UI state)             interface)       DataStore)
```

- **MVVM + unidirectional data flow.** `WeatherViewModel` exposes a single
  `StateFlow<WeatherUiState>` (`Idle` / `Loading` / `Success` / `Error`) that
  the UI simply renders — no two-way bindings, no hidden state.
- **Repository pattern.** `WeatherRepository` is a domain-layer interface;
  `WeatherRepositoryImpl` is the only class that knows about Retrofit,
  geocoding, or the weather API. The ViewModel never talks to the network
  directly.
- **Dependency injection with Hilt.** `NetworkModule` provides Retrofit/OkHttp
  singletons, `RepositoryModule` binds the repository interface to its
  implementation — all constructor-injected, nothing built manually.
- **Coroutines + Flow** for all async work, with network calls dispatched on
  `Dispatchers.IO`.
- **Jetpack DataStore** persists the last searched city so the app has
  something meaningful to show on the next launch.
- **Material 3 + dynamic color** theming (Material You on Android 12+, a
  hand-picked palette elsewhere), built entirely in Compose.

## Project structure

```
app/src/main/java/com/example/weatherapp/
├── WeatherApplication.kt          # @HiltAndroidApp entry point
├── MainActivity.kt                # Single Activity, hosts Compose content
├── data/
│   ├── remote/
│   │   ├── WeatherApiService.kt       # Retrofit interface — forecast endpoint
│   │   ├── GeocodingApiService.kt     # Retrofit interface — geocoding endpoint
│   │   └── dto/                       # Raw API response models (@Serializable)
│   ├── repository/
│   │   └── WeatherRepositoryImpl.kt   # Combines geocoding + forecast calls
│   └── local/
│       └── PreferencesManager.kt      # DataStore wrapper (last searched city)
├── domain/
│   ├── model/                     # Clean domain models (WeatherInfo, LocationInfo)
│   └── repository/
│       └── WeatherRepository.kt   # Repository contract (interface)
├── di/
│   ├── NetworkModule.kt           # Retrofit / OkHttp / Json providers
│   └── RepositoryModule.kt        # Binds WeatherRepository -> WeatherRepositoryImpl
├── util/
│   └── WeatherCodeMapper.kt       # WMO weather code -> description + emoji
└── presentation/
    ├── weather/
    │   ├── WeatherViewModel.kt    # Holds UI state, calls the repository
    │   ├── WeatherUiState.kt      # Sealed UI state (Idle/Loading/Success/Error)
    │   └── WeatherScreen.kt       # Compose UI (search field + result card)
    └── theme/
        ├── Color.kt
        ├── Theme.kt
        └── Type.kt
```

## Requirements

- Android Studio Ladybug (2024.2.1) or newer
- JDK 17 (bundled with recent Android Studio versions)
- An Android device or emulator running **API 24+** with internet access

## Getting started

1. Open the `WeatherApp/` folder in Android Studio (**File → Open**).
2. Let Gradle sync — it will download the dependencies listed in
   `app/build.gradle.kts`. If Android Studio prompts you to regenerate the
   Gradle wrapper jar, accept it (or run `gradle wrapper` once from the
   command line if you have a local Gradle install).
3. Run the `app` configuration on an emulator or physical device.
4. Type a city name (e.g. `Tokyo`, `Cairo`, `São Paulo`) and hit search / the
   IME search action.

No API keys, `local.properties` secrets, or backend setup required.

## Notable UX details

- Loading, error, and empty states are handled explicitly — the UI never
  shows a blank screen while data is in flight.
- Errors are surfaced in plain language (e.g. "No results found for..."
  rather than a raw HTTP exception).
- The keyboard's search action triggers a lookup, so users aren't forced to
  tap the search icon.
- The last successful search is restored automatically on next launch via
  DataStore, so returning users see data immediately.


