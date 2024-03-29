package gabor.koleszar.dougscore.data.local.preferences

import android.content.SharedPreferences
import gabor.koleszar.dougscore.domain.model.UserSettings
import gabor.koleszar.dougscore.domain.preferences.Preferences

class PreferencesImpl(
	private val sharedPref: SharedPreferences
) : Preferences {

	override fun saveLastTimeDataUpdated(timeInMillis: Long) {
		sharedPref.edit()
			.putLong(Preferences.KEY_LAST_UPDATE, timeInMillis)
			.apply()
	}

	override fun loadLastTimeDataUpdated(): Long {
		return sharedPref.getLong(
			Preferences.KEY_LAST_UPDATE,
			System.currentTimeMillis()
		)
	}

	override fun saveUseDarkTheme(useDarkTheme: Boolean) {
		sharedPref.edit()
			.putBoolean(Preferences.KEY_DARK_THEME, useDarkTheme)
			.apply()
	}

	override fun saveUseDeviceTheme(useDeviceTheme: Boolean) {
		sharedPref.edit()
			.putBoolean(Preferences.KEY_DEVICE_THEME, useDeviceTheme)
			.apply()
	}


	override fun loadUserSettings(): UserSettings {
		val useDarkTheme = sharedPref.getBoolean(Preferences.KEY_DARK_THEME, false)
		val useDeviceTheme = sharedPref.getBoolean(Preferences.KEY_DEVICE_THEME, true)

		return UserSettings(useDarkTheme, useDeviceTheme)
	}
}