package gabor.koleszar.dougscore.domain.preferences

import gabor.koleszar.dougscore.domain.model.UserSettings

interface Preferences {
	fun saveLastTimeDataUpdated(timeInMillis: Long)

	fun loadLastTimeDataUpdated(): Long

	fun saveUseDarkTheme(useDarkTheme: Boolean)

	fun saveUseDeviceTheme(useDeviceTheme: Boolean)

	fun loadUserSettings(): UserSettings

	companion object {
		const val KEY_LAST_UPDATE = "lastupdatekey"
		const val KEY_DARK_THEME = "darkthemekey"
		const val KEY_DEVICE_THEME = "devicethemekey"
	}
}