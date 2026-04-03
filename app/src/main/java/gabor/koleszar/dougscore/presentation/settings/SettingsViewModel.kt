package gabor.koleszar.dougscore.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gabor.koleszar.dougscore.domain.repository.CarRepository
import gabor.koleszar.dougscore.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
	private val userPreferencesRepository: UserPreferencesRepository,
	private val carRepository: CarRepository
) : ViewModel() {

	private val _settingsState = MutableStateFlow(SettingsState())
	val settingsState = _settingsState.asStateFlow()

	init {
		viewModelScope.launch {
			loadUserSettings()
		}
		viewModelScope.launch {
			userPreferencesRepository.loadLastTimeDataUpdated().collectLatest { newTimeStamp ->
				_settingsState.update {
					it.copy(
						lastUpdatedTimeStamp = newTimeStamp
					)
				}
			}
		}
	}

	suspend fun loadUserSettings() {
		userPreferencesRepository.loadUserSettings().collectLatest { newUserSettings ->
			_settingsState.update {
				it.copy(
					useDarkTheme = newUserSettings.useDarkTheme,
					useDeviceTheme = newUserSettings.useDeviceTheme,
					useDynamicColor = newUserSettings.useDynamicColor,
					initialState = false
				)
			}
		}
	}

	fun onAction(event: SettingsAction) {
		viewModelScope.launch {
			when (event) {
				SettingsAction.ToggleDarkTheme -> {
					userPreferencesRepository.saveUseDarkTheme(!settingsState.value.useDarkTheme)
				}

				SettingsAction.ToggleDeviceTheme -> {
					userPreferencesRepository.saveUseDeviceTheme(!settingsState.value.useDeviceTheme)
				}

				SettingsAction.ToggleDynamicColor -> {
					userPreferencesRepository.saveUseDynamicColor(!settingsState.value.useDynamicColor)
				}

				SettingsAction.RefreshCars -> {
					_settingsState.update { it.copy(isLoading = true) }
					carRepository.downloadCars()
					_settingsState.update { it.copy(isLoading = false) }
				}
			}
		}
	}
}