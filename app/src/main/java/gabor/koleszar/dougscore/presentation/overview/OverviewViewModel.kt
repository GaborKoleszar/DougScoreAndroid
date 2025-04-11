package gabor.koleszar.dougscore.presentation.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gabor.koleszar.dougscore.domain.repository.CarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
	private val carRepository: CarRepository
) : ViewModel() {

	private val _state = MutableStateFlow(OverviewState())
	val state = _state.asStateFlow()
		.onStart {
			getCars()
		}
		.map { updatedState ->
			var filteredCars = updatedState.cars

			if (updatedState.searchQuery.length >= 3) {
				println("KEY Filterezés ${updatedState.searchQuery}")
				filteredCars = updatedState.cars.filter { car ->
					car.doesMatchSearchQuery(updatedState.searchQuery.lowercase())
				}
			}
			if (updatedState.isDescending) {
				filteredCars = filteredCars.reversed()
			}

			println("KEY Frissül a lista ${updatedState.searchQuery}")
			return@map updatedState.copy(cars = filteredCars)
		}
		.stateIn(
			viewModelScope,
			SharingStarted.WhileSubscribed(5_000L),
			_state.value
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
						println("KEY Beállítódik a search ${action.searchQuery}")
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

	private fun downloadCars() {
		viewModelScope.launch {
			val successful = carRepository.downloadCars()
			//TODO add error toast if failed download
		}
	}

	private fun getCars(
	) {
		viewModelScope.launch {
			carRepository
				.getCars()
				.collectLatest { carList ->
					_state.update {
						it.copy(
							cars = carList,
							isLoading = false
						)
					}
				}
		}
	}
}