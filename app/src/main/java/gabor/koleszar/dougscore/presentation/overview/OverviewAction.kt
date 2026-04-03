package gabor.koleszar.dougscore.presentation.overview

sealed interface OverviewAction {
    data object ToggleIsDescending : OverviewAction
    data object ClearSearchField : OverviewAction
    data object RefreshList : OverviewAction
    data object DescriptionClick : OverviewAction
    data object ClearManufacturerFilters : OverviewAction
    data object ClearCountryFilters : OverviewAction
    data class CarClick(val carId: Int) : OverviewAction
    data class SearchTextChange(val searchQuery: String) : OverviewAction
    data class ToggleManufacturerFilter(val manufacturer: String) : OverviewAction
    data class ToggleCountryFilter(val country: String) : OverviewAction
}