package gabor.koleszar.dougscore.presentation.compare

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import gabor.koleszar.dougscore.domain.repository.CarRepository
import gabor.koleszar.dougscore.presentation.Route
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CompareResultViewModel @Inject constructor(
    private val repository: CarRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val carId1 = savedStateHandle.toRoute<Route.CompareResult>().carId1
    private val carId2 = savedStateHandle.toRoute<Route.CompareResult>().carId2

    val state: StateFlow<CompareResultState> = repository.getCarWithId(carId1)
        .combine(repository.getCarWithId(carId2)) { car1, car2 ->
            CompareResultState(
                car1 = car1,
                car2 = car2,
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            CompareResultState()
        )
}
