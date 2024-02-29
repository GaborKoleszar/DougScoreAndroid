package gabor.koleszar.dougscore.domain.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class Car(
	val id: Int,
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
) {
	suspend fun doesMatchSearchQuery(searchQuery: String): Boolean {
		return withContext(Dispatchers.Default) {
			return@withContext manufacturer.lowercase().contains(searchQuery) ||
					model.lowercase().contains(searchQuery)
		}
	}
}
