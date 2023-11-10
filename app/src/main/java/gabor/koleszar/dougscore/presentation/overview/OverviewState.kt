package gabor.koleszar.dougscore.presentation.overview

import gabor.koleszar.dougscore.domain.model.Car

data class OverviewState(
	val cars: List<Car> = emptyList(),
	val isLoading: Boolean = false,
	val isRefreshing: Boolean = false
)
