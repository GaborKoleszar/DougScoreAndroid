package gabor.koleszar.dougscore.domain.model

import gabor.koleszar.dougscore.common.Constants
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
	val videoId: String?,
	val filmingLocationCity: String,
	val filmingLocationState: String,
	val vehicleCountry: String
) {
	suspend fun doesMatchSearchQuery(searchQuery: String): Boolean {
		return withContext(Dispatchers.Default) {
			return@withContext ("${manufacturer.lowercase()} ${model.lowercase()}")
				.contains(searchQuery.lowercase())
		}
	}

	fun getSdImageLink(): String {
		return Constants.YT_IMAGE_URL + videoId + Constants.YT_IMAGE_SDDEFAULT
	}

	fun getHqFallbackImageLink(): String {
		return Constants.YT_IMAGE_FALLBACK_URL + videoId + Constants.YT_IMAGE_HQDEFAULT
	}

	fun getMaxresImageLink(): String {
		return Constants.YT_IMAGE_URL + videoId + Constants.YT_IMAGE_MAXRESDEFAULT
	}
}
