package gabor.koleszar.dougscore.presentation

import kotlinx.serialization.Serializable

sealed interface Route {

	@Serializable
	data object OverView : Route

	@Serializable
	data object Settings : Route

	@Serializable
	data object Description : Route

	@Serializable
	data class Details(val carId: Int) : Route

	@Serializable
	data class CarPicker(val fromCarId: Int) : Route

	@Serializable
	data class CompareResult(val carId1: Int, val carId2: Int) : Route
}