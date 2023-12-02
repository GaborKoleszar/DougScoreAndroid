package gabor.koleszar.dougscore.presentation.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gabor.koleszar.dougscore.common.Resource
import gabor.koleszar.dougscore.domain.repository.CarRepository
import gabor.koleszar.dougscore.presentation.details.DetailsState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val repository: CarRepository
) : ViewModel() {
	var overviewState by mutableStateOf(OverviewState())
		private set

	var detailsState by mutableStateOf(DetailsState())

	init {
		getCarIntroductions()
	}

	fun refresh() {
		getCarIntroductions(true)
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
								overviewState = overviewState.copy(
									cars = freshCars,
									isLoading = false,
									isRefreshing = false,
								)
							}
						}

						is Resource.Error -> {
							overviewState = overviewState.copy(
								isLoading = false,
								isRefreshing = false,
							)
						}

						is Resource.Loading -> {
							overviewState = overviewState.copy(
								isLoading = true,
								isRefreshing = true,
							)
						}
					}
				}
		}
	}
}