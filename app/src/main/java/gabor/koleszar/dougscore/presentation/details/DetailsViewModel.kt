package gabor.koleszar.dougscore.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import gabor.koleszar.dougscore.domain.model.Car
import gabor.koleszar.dougscore.domain.repository.CarRepository
import gabor.koleszar.dougscore.presentation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
	private val repository: CarRepository,
	savedStateHandle: SavedStateHandle
) : ViewModel() {

	private val _selectedCarId = savedStateHandle.toRoute<Route.Details>().carId

	private val _carInDetailsScreen = MutableStateFlow<Car?>(null)
	val carInDetailsScreen = _carInDetailsScreen.asStateFlow()
		.onStart {
			setCarInDetails(_selectedCarId)
		}.stateIn(
			viewModelScope,
			SharingStarted.WhileSubscribed(5_000L),
			_carInDetailsScreen.value
		)

	private fun setCarInDetails(id: Int) {
		viewModelScope.launch {
			repository.getCarWithId(id).collectLatest { resultCar ->
				resultCar.let { car ->
					_carInDetailsScreen.update { car }
				}
			}
		}
	}
}