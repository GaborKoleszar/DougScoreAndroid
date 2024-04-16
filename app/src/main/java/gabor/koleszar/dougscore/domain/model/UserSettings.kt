package gabor.koleszar.dougscore.domain.model

data class UserSettings(
	val useDarkTheme: Boolean = false,
	val useDeviceTheme: Boolean = true,
	val useDynamicColor: Boolean = false
)
