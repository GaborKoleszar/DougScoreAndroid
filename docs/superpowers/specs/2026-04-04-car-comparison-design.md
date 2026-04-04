# Car Comparison Feature — Design Spec

**Date:** 2026-04-04  
**Branch:** `feature/comparison`

## Context

DougScore Android lets users browse and view detailed scores for cars reviewed by Doug DeMuro. Users wanted to compare two cars side-by-side. This feature adds a comparison flow accessible from the car Details screen.

## User Flow

```
Details(carId=X)
  → [Compare button]
  → CarPicker(fromCarId=X)   [searchable compact list, excludes car X]
  → [tap a car]
  → CompareResult(carId1=X, carId2=Y)  [split top/bottom view]
```

## Navigation & Routes

Two new typed routes added to `presentation/Route.kt`:

```kotlin
@Serializable data class CarPicker(val fromCarId: Int) : Route
@Serializable data class CompareResult(val carId1: Int, val carId2: Int) : Route
```

An `OutlinedButton` labeled "Compare" was added to `DetailsScreen.kt` alongside the existing YouTube button. On click: `navController.navigate(Route.CarPicker(car.id))`.

Both routes are wired into the `NavHost` in `MainActivity.kt`.

## Car Picker Screen

Files: `presentation/compare/CarPickerScreen.kt`, `CarPickerViewModel.kt`, `CarPickerState.kt`, `CarPickerAction.kt`

- **Layout:** `OutlinedTextField` (search) pinned at top + `LazyColumn` below
- **List item:** `CompactCarListItem` (70dp height) — thumbnail + manufacturer + model + DougScore
- **Filtering:** Real-time using `Car.doesMatchSearchQuery()` when query is non-empty, excludes `fromCarId` car
- **State:** `CarPickerState(cars: List<Car>, searchQuery: String, isLoading: Boolean)`
- **Actions:** `SearchTextChange(query: String)`, `CarClick(carId: Int)`
- **Navigation:** `CarClick` handled in `MainActivity` → `navigate(Route.CompareResult(fromCarId, selectedCarId))`

## Compare Result Screen

Files: `presentation/compare/CompareResultScreen.kt`, `CompareResultViewModel.kt`, `CompareResultState.kt`

- **Layout:** Vertically scrollable `Column`, two car panels stacked top/bottom with `HorizontalDivider` between them
- **Each panel** (`CarComparePanel`):
  1. `AsyncImageWithMultipleFallback` (300–600dp wide, FillWidth)
  2. Manufacturer + Model text (bold)
  3. Daily/Weekend column headers
  4. `CompareScoreTable(thisCar = car, otherCar = otherCar)`
- **CompareScoreTable** (in `presentation/components/DetailScreenComponents.kt`): Accepts `thisCar: Car` and `otherCar: Car`. Displays `thisCar`'s scores colored relative to `otherCar`:
  - Win → `ScoreGreen`, Loss → `ScoreRed`, Tie → neutral (`onSurface`)
  - Uses private `compareColor(mine: Int, theirs: Int)` helper
  - Top panel: `(car1, car2)`, bottom panel: `(car2, car1)`
- **Win/loss logic** lives in the composable layer — ViewModel stays pure
- **State:** `CompareResultState(car1: Car?, car2: Car?, isLoading: Boolean)` — `isLoading = car1 == null || car2 == null`
- **ViewModel:** Combines `getCarWithId(carId1)` and `getCarWithId(carId2)` flows via `.combine()`

## Key Files Changed

| File | Change |
|------|--------|
| `presentation/Route.kt` | Added `CarPicker` and `CompareResult` routes |
| `presentation/MainActivity.kt` | Wired 2 new composable destinations, handle `CarClick` |
| `presentation/details/DetailsScreen.kt` | Added `onCompareClick` param + `OutlinedButton` |
| `presentation/components/DetailScreenComponents.kt` | Added `CompareScoreTable` + `compareColor` |
| `presentation/compare/` (new) | `CarPickerState`, `CarPickerAction`, `CarPickerViewModel`, `CarPickerScreen`, `CompareResultState`, `CompareResultViewModel`, `CompareResultScreen` |
| `res/values/strings.xml` | Added `compare_button_label`, `car_picker_search_hint`, `compare_dougscore_label` |
