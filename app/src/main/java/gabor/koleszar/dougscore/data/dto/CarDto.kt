package gabor.koleszar.dougscore.data.dto

import gabor.koleszar.dougscore.domain.model.DailyScore
import gabor.koleszar.dougscore.domain.model.WeekendScore

data class CarDto(
	val id: Int,
	val year: Int,
	val manufacturer: String,
	val model: String,
	val weekendScore: WeekendScore,
	val dailyScore: DailyScore,
	val dougScore: Int,
	val videoLink: String?,
	val videoId: String?,
	val filmingLocationCity: String,
	val filmingLocationState: String,
	val vehicleCountry: String
)
