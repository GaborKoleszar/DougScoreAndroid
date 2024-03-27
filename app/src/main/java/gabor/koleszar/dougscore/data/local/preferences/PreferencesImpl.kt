package gabor.koleszar.dougscore.data.local.preferences

import android.content.SharedPreferences
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
}