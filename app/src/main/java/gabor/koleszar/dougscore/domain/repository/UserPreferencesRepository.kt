package gabor.koleszar.dougscore.domain.repository

import gabor.koleszar.dougscore.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
	suspend fun saveLastTimeDataUpdated(timeInMillis: Long)

	suspend fun loadLastTimeDataUpdated(): Flow<Long>

	suspend fun saveUseDarkTheme(useDarkTheme: Boolean)

	suspend fun saveUseDeviceTheme(useDeviceTheme: Boolean)

	suspend fun saveUseDynamicColor(useDynamicColor: Boolean)

	suspend fun loadUserSettings(): Flow<UserSettings>

	companion object {
		const val KEY_LAST_UPDATE = "lastupdatekey"
		const val KEY_DARK_THEME = "darkthemekey"
		const val KEY_DEVICE_THEME = "devicethemekey"
		const val KEY_DYNAMIC_COLOR = "dynamiccolorkey"
	}
}