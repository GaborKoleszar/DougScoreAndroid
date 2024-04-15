package gabor.koleszar.dougscore.domain.preferences

import gabor.koleszar.dougscore.domain.model.UserSettings

interface UserPreferences {
	suspend fun saveLastTimeDataUpdated(timeInMillis: Long)

	suspend fun loadLastTimeDataUpdated(): Long

	suspend fun saveUseDarkTheme(useDarkTheme: Boolean)

	suspend fun saveUseDeviceTheme(useDeviceTheme: Boolean)

	suspend fun loadUserSettings(): UserSettings

	companion object {
		const val KEY_LAST_UPDATE = "lastupdatekey"
		const val KEY_DARK_THEME = "darkthemekey"
		const val KEY_DEVICE_THEME = "devicethemekey"
	}
}