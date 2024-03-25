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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val repository: CarRepository
) : ViewModel() {

	private val _searchText = MutableStateFlow("")
	val searchText = _searchText.asStateFlow()

	private val _cars = MutableStateFlow<List<Car>>(emptyList())

	@OptIn(FlowPreview::class)
	val cars = searchText.debounce(500L)
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

	var isRefreshing by mutableStateOf(false)
		private set

	private val _carInDetailsScreen = MutableStateFlow<Car?>(null)
	val carInDetailsScreen = _carInDetailsScreen.asStateFlow()

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

	fun onCarSelected(carId: Int) {
		_carInDetailsScreen.update {
			cars.value[carId]
		}
	}

	private fun getCarIntroductions(
		shouldFetchFromRemote: Boolean = false
	) {
		viewModelScope.launch {
			repository
				.getAllCars(shouldFetchFromRemote)
				.collect { result ->
					when (result) {

						is Resource.Success -> {
							result.data?.let { freshCars ->
								_cars.update { freshCars }
								isLoading = false
								isRefreshing = false
							}
						}

						is Resource.Error -> {
							isLoading = false
							isRefreshing = false
						}

						is Resource.Loading -> {
							isLoading = true
							isRefreshing = true
						}
					}
				}
		}
	}
}