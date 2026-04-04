package gabor.koleszar.dougscore.presentation.overview

import gabor.koleszar.dougscore.domain.model.Car

data class OverviewState(
    val cars: List<Car> = emptyList(),
    val isLoading: Boolean = true,
    val isDescending: Boolean = false,
    val searchQuery: String = "",
    val availableManufacturers: List<String> = emptyList(),
    val availableCountries: List<String> = emptyList(),
    val selectedManufacturers: Set<String> = emptySet(),
    val selectedCountries: Set<String> = emptySet(),
)
