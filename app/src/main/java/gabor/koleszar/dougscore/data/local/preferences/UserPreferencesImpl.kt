package gabor.koleszar.dougscore.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import gabor.koleszar.dougscore.domain.model.UserSettings
import gabor.koleszar.dougscore.domain.preferences.UserPreferences
import kotlinx.coroutines.flow.first

class UserPreferencesImpl(
	private val dataStore: DataStore<Preferences>
) : UserPreferences {

	override suspend fun saveLastTimeDataUpdated(timeInMillis: Long) {
		val key = longPreferencesKey(UserPreferences.KEY_LAST_UPDATE)
		dataStore.edit { prefs ->
			prefs[key] = timeInMillis
		}
	}

	override suspend fun loadLastTimeDataUpdated(): Long {
		val key = longPreferencesKey(UserPreferences.KEY_LAST_UPDATE)
		return dataStore.data.first()[key] ?: System.currentTimeMillis()
	}

	override suspend fun saveUseDarkTheme(useDarkTheme: Boolean) {
		val key = booleanPreferencesKey(UserPreferences.KEY_DARK_THEME)
		dataStore.edit { prefs ->
			prefs[key] = useDarkTheme
		}
	}

	override suspend fun saveUseDeviceTheme(useDeviceTheme: Boolean) {
		val key = booleanPreferencesKey(UserPreferences.KEY_DEVICE_THEME)
		dataStore.edit { prefs ->
			prefs[key] = useDeviceTheme
		}
	}


	override suspend fun loadUserSettings(): UserSettings {
		val keyDarkTheme = booleanPreferencesKey(UserPreferences.KEY_DARK_THEME)
		val keyDeviceTheme = booleanPreferencesKey(UserPreferences.KEY_DEVICE_THEME)

		val darkTheme = dataStore.data.first()[keyDarkTheme] ?: false
		val deviceTheme = dataStore.data.first()[keyDeviceTheme] ?: false

		return UserSettings(darkTheme, deviceTheme)
	}
}