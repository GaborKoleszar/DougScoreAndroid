package gabor.koleszar.dougscore.presentation.compare

sealed interface CarPickerAction {
    data class SearchTextChange(val query: String) : CarPickerAction
    data class CarClick(val carId: Int) : CarPickerAction
}
