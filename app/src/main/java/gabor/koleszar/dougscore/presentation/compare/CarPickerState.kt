package gabor.koleszar.dougscore.presentation.compare

import gabor.koleszar.dougscore.domain.model.Car

data class CarPickerState(
    val cars: List<Car> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
)
