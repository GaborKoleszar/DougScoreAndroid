# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
./gradlew assembleDebug          # Build debug APK
./gradlew assembleRelease        # Build release APK (minified + resource shrinking)
./gradlew test                   # Unit tests
./gradlew connectedAndroidTest   # Instrumented tests (requires device/emulator)
./gradlew lint                   # Android Lint checks
./gradlew clean                  # Clean build artifacts
```

## Architecture

This is a Clean Architecture Android app with three layers:

**Data layer** (`data/`) — Fetches an XLSX file from Google Sheets via Retrofit, parses it with Apache POI, caches in Room (`car_db`), and stores user preferences via DataStore. `CarRepositoryImpl` fetches remotely on first call and reads from cache thereafter.

**Domain layer** (`domain/`) — Pure Kotlin models (`Car`, `DailyScore`, `WeekendScore`, `UserSettings`) and repository interfaces. No Android dependencies.

**Presentation layer** (`presentation/`) — Jetpack Compose UI with ViewModels. Uses MVI-style Actions → StateFlow for the Overview screen; simpler MVVM with StateFlow for Details and Settings screens.

### Data flow
Remote XLSX → Apache POI parser → DTOs → `CarMapper` → Room entities → domain models → `StateFlow` → Compose UI

### Key files
- `common/Resource.kt` — Sealed class `Success/Error/Loading` used throughout
- `presentation/Route.kt` — Type-safe navigation routes
- `di/ProviderAppModule.kt` / `di/BinderAppModule.kt` — Hilt DI modules

## Tech Stack

- **UI**: Jetpack Compose + Material 3, Navigation Compose
- **DI**: Hilt 2.59.2
- **Local DB**: Room 2.8.4
- **Network**: Retrofit 3 + OkHttp 5, parses XLSX with Apache POI
- **Preferences**: DataStore
- **Images**: Coil 2.7.0
- **Analytics/Crashes**: Firebase Analytics + Crashlytics
- **Performance**: Baseline profiles (`:baselineprofile` module)

## Module Structure

| Module | Purpose |
|--------|---------|
| `:app` | Main application (minSdk 26, targetSdk 36) |
| `:baselineprofile` | Generates startup baseline profiles |

Dependencies are managed centrally in `gradle/libs.versions.toml`.
