package gabor.koleszar.dougscore.presentation.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gabor.koleszar.dougscore.common.Resource
import gabor.koleszar.dougscore.domain.model.Car
import gabor.koleszar.dougscore.domain.repository.CarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
	private val carRepository: CarRepository
) : ViewModel() {

	private val _searchText = MutableStateFlow("")
	val searchText = _searchText.asStateFlow()

	private val _isDescending = MutableStateFlow(false)
	val isDescending = _isDescending.asStateFlow()

	private val _isLoading = MutableStateFlow(true)
	val isLoading = _isLoading.asStateFlow()

	private val _cars = MutableStateFlow<List<Car>>(emptyList())

	val cars = combine(_searchText, _cars, _isDescending) { query, cars, isDescending ->
		var filteredCars = cars

		if (query.length >= 3) {
			filteredCars = cars.filter { car ->
				car.doesMatchSearchQuery(query.lowercase())
			}
		}
		if (isDescending) {
			filteredCars = filteredCars.reversed()
		}

		return@combine filteredCars
	}.stateIn(
		viewModelScope,
		SharingStarted.WhileSubscribed(5000),
		_cars.value
	)

	init {
		getCarIntroductions()
	}

	fun onSearchTextChange(text: String) {
		_searchText.update { text }
	}

	fun refresh() {
		getCarIntroductions(true)
	}

	fun onClearSearchField() {
		_searchText.update { "" }
	}

	fun handleEvent(event: OverviewEvent) {
		viewModelScope.launch {
			when (event) {
				OverviewEvent.TOGGLE_IS_DESCENDING -> {
					_isDescending.update { !isDescending.value }
				}
			}
		}
	}

	private fun getCarIntroductions(
		shouldFetchFromRemote: Boolean = false
	) {
		viewModelScope.launch {
			carRepository
				.getAllCars(shouldFetchFromRemote)
				.collectLatest { result ->
					when (result) {

						is Resource.Success -> {
							result.data?.let { freshCars ->
								_cars.update { freshCars }
								_isLoading.update { false }
							}
						}

						is Resource.Error -> {
							_isLoading.update { false }
						}

						is Resource.Loading -> {
							_isLoading.update { true }
						}
					}
				}
		}
	}
}