package gabor.koleszar.dougscore.presentation.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gabor.koleszar.dougscore.domain.model.Car
import gabor.koleszar.dougscore.domain.repository.CarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val carRepository: CarRepository
) : ViewModel() {

    private val _carsFromRepo = carRepository.getCars().stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _state = MutableStateFlow(OverviewState())
    val state = _state
        .combine(_carsFromRepo) { state, cars ->
            updateCarsList(state, cars)
        }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = OverviewState()
        )

    fun onAction(action: OverviewAction) {
        viewModelScope.launch {
            when (action) {
                is OverviewAction.ToggleIsDescending -> {
                    _state.update {
                        it.copy(
                            isDescending = !it.isDescending
                        )
                    }
                }

                is OverviewAction.ClearSearchField -> {
                    _state.update {
                        it.copy(
                            searchQuery = ""
                        )
                    }
                }

                is OverviewAction.RefreshList -> {
                    downloadCars()
                }

                is OverviewAction.SearchTextChange -> {
                    _state.update {
                        it.copy(
                            searchQuery = action.searchQuery
                        )
                    }
                }

                else -> {
                    // Navigation actions handled in MainActivity
                }
            }
        }
    }

    private fun updateCarsList(newState: OverviewState, cars: List<Car>): OverviewState {
        var filteredCars = cars
        if (newState.searchQuery.length > 3) {
            filteredCars = cars.filter { car ->
                car.doesMatchSearchQuery(newState.searchQuery.lowercase())
            }
        }

        return newState.copy(
            cars = if (newState.isDescending) filteredCars.reversed() else filteredCars,
            isLoading = false
        )
    }

    private fun downloadCars() {
        viewModelScope.launch {
            val successful = carRepository.downloadCars()
            //TODO add error toast if failed download
        }
    }
}
