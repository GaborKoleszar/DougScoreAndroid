package gabor.koleszar.dougscore.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gabor.koleszar.dougscore.common.Resource
import gabor.koleszar.dougscore.domain.model.Car
import gabor.koleszar.dougscore.domain.repository.CarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
	private val repository: CarRepository
) : ViewModel() {

	private val _carInDetailsScreen = MutableStateFlow<Car?>(null)
	val carInDetailsScreen = _carInDetailsScreen.asStateFlow()

	fun setCarInDetails(id: Int) {
		viewModelScope.launch {
			repository.getCarWithId(id).collectLatest { resultCar ->
				when (resultCar) {
					is Resource.Success -> {
						resultCar.data?.let {car ->
							_carInDetailsScreen.update { car }
						}
					}
					is Resource.Error -> {}
					is Resource.Loading -> {}
				}
			}
		}
	}
}