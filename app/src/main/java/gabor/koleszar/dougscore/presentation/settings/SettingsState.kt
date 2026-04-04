package gabor.koleszar.dougscore.presentation.settings

data class SettingsState(
	val useDarkTheme: Boolean = false,
	val useDeviceTheme: Boolean = false,
	val useDynamicColor: Boolean = false,
	val isLoading: Boolean = false,
	val initialState: Boolean = true,
	val lastUpdatedTimeStamp: Long = Long.MIN_VALUE
)
