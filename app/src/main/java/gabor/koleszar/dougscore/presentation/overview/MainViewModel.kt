package gabor.koleszar.dougscore.presentation.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gabor.koleszar.dougscore.common.Resource
import gabor.koleszar.dougscore.domain.repository.CarRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val repository: CarRepository
) : ViewModel() {
	var state by mutableStateOf(OverviewState())
		private set

	init {
		getCarIntroductions()
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
								state = state.copy(
									cars = freshCars,
									isLoading = false
								)
							}
						}

						is Resource.Error -> Unit
						is Resource.Loading -> {
							state = state.copy(
								isLoading = true
							)
						}
					}
				}
		}
	}
}