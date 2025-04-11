package gabor.koleszar.dougscore.presentation.overview

sealed interface OverviewAction {
	data object ToggleIsDescending : OverviewAction
	data object ClearSearchField : OverviewAction
	data object RefreshList : OverviewAction
	data object DescriptionClick : OverviewAction
	data class CarClick(val carId: Int) : OverviewAction
	data class SearchTextChange(val searchQuery: String) : OverviewAction
}