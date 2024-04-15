package gabor.koleszar.dougscore.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gabor.koleszar.dougscore.domain.model.UserSettings
import gabor.koleszar.dougscore.domain.preferences.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
	private val userPreferences: UserPreferences
) : ViewModel() {

	private val _userSettings = MutableStateFlow(UserSettings())
	val userSettings = _userSettings.asStateFlow()

	private val _lastUpdatedTimeStamp = MutableStateFlow(System.currentTimeMillis())
	val lastUpdatedTimeStamp = _lastUpdatedTimeStamp.asStateFlow()

	init {
		viewModelScope.launch {
			_userSettings.emit(userPreferences.loadUserSettings())
			_lastUpdatedTimeStamp.emit(userPreferences.loadLastTimeDataUpdated())
		}
	}
}