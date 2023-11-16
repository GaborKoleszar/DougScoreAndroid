package gabor.koleszar.dougscore.domain.model

data class Car(
	val year: Int,
	val manufacturer: String,
	val model: String,
	val weekendScore: WeekendScore,
	val dailyScore: DailyScore,
	val dougScore: Int,
	val videoLink: String?,
	val imageLink: String?,
	val filmingLocationCity: String,
	val filmingLocationState: String,
	val vehicleCountry: String
)
