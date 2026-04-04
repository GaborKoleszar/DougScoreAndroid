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
                    _state.update { it.copy(isDescending = !it.isDescending) }
                }
                is OverviewAction.ClearSearchField -> {
                    _state.update { it.copy(searchQuery = "") }
                }
                is OverviewAction.RefreshList -> {
                    downloadCars()
                }
                is OverviewAction.SearchTextChange -> {
                    _state.update { it.copy(searchQuery = action.searchQuery) }
                }
                is OverviewAction.ToggleManufacturerFilter -> {
                    _state.update {
                        val newSet = if (action.manufacturer in it.selectedManufacturers)
                            it.selectedManufacturers - action.manufacturer
                        else
                            it.selectedManufacturers + action.manufacturer
                        it.copy(selectedManufacturers = newSet)
                    }
                }
                is OverviewAction.ToggleCountryFilter -> {
                    _state.update {
                        val newSet = if (action.country in it.selectedCountries)
                            it.selectedCountries - action.country
                        else
                            it.selectedCountries + action.country
                        it.copy(selectedCountries = newSet)
                    }
                }
                is OverviewAction.ClearManufacturerFilters -> {
                    _state.update { it.copy(selectedManufacturers = emptySet()) }
                }
                is OverviewAction.ClearCountryFilters -> {
                    _state.update { it.copy(selectedCountries = emptySet()) }
                }
                else -> {
                    // Navigation actions handled in MainActivity
                }
            }
        }
    }

    private fun updateCarsList(newState: OverviewState, cars: List<Car>): OverviewState {
        val availableManufacturers = cars.map { it.manufacturer }.distinct().sorted()
        val availableCountries = cars.map { it.vehicleCountry }.distinct().sorted()

        // Drop stale selections after a data refresh
        val cleanedManufacturers = newState.selectedManufacturers.intersect(availableManufacturers.toSet())
        val cleanedCountries = newState.selectedCountries.intersect(availableCountries.toSet())

        var filteredCars = cars
        if (newState.searchQuery.length > 3) {
            filteredCars = filteredCars.filter { it.doesMatchSearchQuery(newState.searchQuery.lowercase()) }
        }
        if (cleanedManufacturers.isNotEmpty()) {
            filteredCars = filteredCars.filter { it.manufacturer in cleanedManufacturers }
        }
        if (cleanedCountries.isNotEmpty()) {
            filteredCars = filteredCars.filter { it.vehicleCountry in cleanedCountries }
        }

        return newState.copy(
            cars = if (newState.isDescending) filteredCars.reversed() else filteredCars,
            isLoading = if (cars.isNotEmpty()) false else newState.isLoading,
            availableManufacturers = availableManufacturers,
            availableCountries = availableCountries,
            selectedManufacturers = cleanedManufacturers,
            selectedCountries = cleanedCountries,
        )
    }

    private fun downloadCars() {
        viewModelScope.launch {
            val successful = carRepository.downloadCars()
            //TODO add error toast if failed download
        }
    }
}
