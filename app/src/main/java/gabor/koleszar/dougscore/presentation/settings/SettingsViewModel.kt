package gabor.koleszar.dougscore.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import gabor.koleszar.dougscore.domain.preferences.Preferences
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
	private val preferences: Preferences
) : ViewModel() {

	var useDarkTheme by mutableStateOf(false)

	var useDeviceTheme by mutableStateOf(true)

	init {
		val userSettings = preferences.loadUserSettings()
		useDarkTheme = userSettings.useDarkTheme
		useDeviceTheme = userSettings.useDeviceTheme
	}

}