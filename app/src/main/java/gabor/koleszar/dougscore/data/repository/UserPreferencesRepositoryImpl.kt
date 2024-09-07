package gabor.koleszar.dougscore.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import gabor.koleszar.dougscore.domain.model.UserSettings
import gabor.koleszar.dougscore.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepositoryImpl(
	private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

	override suspend fun saveLastTimeDataUpdated(timeInMillis: Long) {
		val key = longPreferencesKey(UserPreferencesRepository.KEY_LAST_UPDATE)
		dataStore.edit { prefs ->
			prefs[key] = timeInMillis
		}
	}

	override fun loadLastTimeDataUpdated(): Flow<Long> {
		val key = longPreferencesKey(UserPreferencesRepository.KEY_LAST_UPDATE)
		return dataStore.data.catch {
			if (it is IOException)
				emit(emptyPreferences())
			else
				throw it
		}.map { preferences ->
			preferences[key] ?: System.currentTimeMillis()
		}
	}

	override suspend fun saveUseDarkTheme(useDarkTheme: Boolean) {
		val key = booleanPreferencesKey(UserPreferencesRepository.KEY_DARK_THEME)
		dataStore.edit { prefs ->
			prefs[key] = useDarkTheme
		}
	}

	override suspend fun saveUseDeviceTheme(useDeviceTheme: Boolean) {
		val key = booleanPreferencesKey(UserPreferencesRepository.KEY_DEVICE_THEME)
		dataStore.edit { prefs ->
			prefs[key] = useDeviceTheme
		}
	}

	override suspend fun saveUseDynamicColor(useDynamicColor: Boolean) {
		val key = booleanPreferencesKey(UserPreferencesRepository.KEY_DYNAMIC_COLOR)
		dataStore.edit { prefs ->
			prefs[key] = useDynamicColor
		}
	}

	override fun loadUserSettings(): Flow<UserSettings> {
		val keyDarkTheme = booleanPreferencesKey(UserPreferencesRepository.KEY_DARK_THEME)
		val keyDeviceTheme = booleanPreferencesKey(UserPreferencesRepository.KEY_DEVICE_THEME)
		val keyDynamicColor = booleanPreferencesKey(UserPreferencesRepository.KEY_DYNAMIC_COLOR)

		return dataStore.data.catch {
			if (it is IOException)
				emit(emptyPreferences())
			else
				throw it
		}.map { preferences ->
			val darkTheme = preferences[keyDarkTheme] ?: false
			val deviceTheme = preferences[keyDeviceTheme] ?: true
			val dynamicColor = preferences[keyDynamicColor] ?: false
			UserSettings(darkTheme, deviceTheme, dynamicColor)
		}
	}
}