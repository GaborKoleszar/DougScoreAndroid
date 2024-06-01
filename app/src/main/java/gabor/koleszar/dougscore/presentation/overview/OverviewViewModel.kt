package gabor.koleszar.dougscore.presentation.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gabor.koleszar.dougscore.common.Resource
import gabor.koleszar.dougscore.domain.model.Car
import gabor.koleszar.dougscore.domain.repository.CarRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
	private val carRepository: CarRepository
) : ViewModel() {

	private val _searchText = MutableStateFlow("")
	val searchText = _searchText.asStateFlow()

	private val _cars = MutableStateFlow<List<Car>>(emptyList())

	val cars = searchText
		.combine(_cars) { text, cars ->
			if (text.isBlank() || text.length < 3) {
				cars
			} else {
				delay(1000L)
				cars.filter { car ->
					car.doesMatchSearchQuery(text.lowercase())
				}
			}
		}
		.stateIn(
			viewModelScope,
			SharingStarted.WhileSubscribed(5000),
			_cars.value
		)

	var isLoading by mutableStateOf(true)
		private set

	init {
		getCarIntroductions()
	}

	fun onSearchTextChange(text: String) {
		_searchText.value = text
	}

	fun refresh() {
		getCarIntroductions(true)
	}

	fun onClearSearchField() {
		_searchText.value = ""
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
								_cars.emit(freshCars)
								isLoading = false
							}
						}

						is Resource.Error -> {
							isLoading = false
						}

						is Resource.Loading -> {
							isLoading = true
						}
					}
				}
		}
	}
}