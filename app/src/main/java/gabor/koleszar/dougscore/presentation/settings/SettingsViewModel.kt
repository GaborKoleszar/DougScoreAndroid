package gabor.koleszar.dougscore.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gabor.koleszar.dougscore.domain.model.UserSettings
import gabor.koleszar.dougscore.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
	private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

	private val _userSettings = MutableStateFlow(UserSettings())
	val userSettings = _userSettings.asStateFlow()

	private val _lastUpdatedTimeStamp = MutableStateFlow(System.currentTimeMillis())
	val lastUpdatedTimeStamp = _lastUpdatedTimeStamp.asStateFlow()

	init {
		viewModelScope.launch {
			userPreferencesRepository.loadUserSettings().collectLatest {
				_userSettings.emit(it)
			}
			userPreferencesRepository.loadLastTimeDataUpdated().collectLatest {
				_lastUpdatedTimeStamp.emit(it)
			}
		}
	}

	fun handleEvent(event: SettingsEvent) {
		viewModelScope.launch {
			when (event) {
				SettingsEvent.TOGGLE_DARK_THEME -> {
					userPreferencesRepository.saveUseDarkTheme(!userSettings.value.useDarkTheme)
				}

				SettingsEvent.TOGGLE_DEVICE_THEME -> {
					userPreferencesRepository.saveUseDeviceTheme(!userSettings.value.useDeviceTheme)
				}
			}
		}
	}
}