package gabor.koleszar.dougscore.domain.preferences

interface Preferences {
	fun saveLastTimeDataUpdated(timeInMillis: Long)

	fun loadLastTimeDataUpdated(): Long

	companion object {
		const val KEY_LAST_UPDATE = "lastupdatekey"
	}
}