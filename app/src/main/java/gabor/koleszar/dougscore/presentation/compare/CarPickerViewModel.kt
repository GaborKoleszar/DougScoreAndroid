package gabor.koleszar.dougscore.presentation.compare

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import gabor.koleszar.dougscore.domain.repository.CarRepository
import gabor.koleszar.dougscore.presentation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarPickerViewModel @Inject constructor(
    private val repository: CarRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val fromCarId = savedStateHandle.toRoute<Route.CarPicker>().fromCarId

    private val _carsFromRepo = repository.getCars().stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _state = MutableStateFlow(CarPickerState())
    val state = _state
        .combine(_carsFromRepo) { state, cars ->
            val query = state.searchQuery
            val filteredCars = cars
                .filter { it.id != fromCarId }
                .let { list ->
                    if (query.isNotEmpty()) {
                        list.filter { it.doesMatchSearchQuery(query.lowercase()) }
                    } else {
                        list
                    }
                }
            state.copy(
                cars = filteredCars,
                isLoading = if (cars.isNotEmpty()) false else state.isLoading,
            )
        }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CarPickerState()
        )

    fun onAction(action: CarPickerAction) {
        viewModelScope.launch {
            when (action) {
                is CarPickerAction.SearchTextChange -> {
                    _state.update { it.copy(searchQuery = action.query) }
                }
                is CarPickerAction.CarClick -> {
                    // Navigation handled in MainActivity
                }
            }
        }
    }
}
