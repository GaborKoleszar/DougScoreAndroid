package gabor.koleszar.dougscore.presentation.settings

sealed interface SettingsAction {
	data object ToggleDarkTheme : SettingsAction
	data object ToggleDeviceTheme : SettingsAction
	data object ToggleDynamicColor : SettingsAction
	data object RefreshCars : SettingsAction
}