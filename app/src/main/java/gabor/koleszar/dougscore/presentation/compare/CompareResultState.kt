package gabor.koleszar.dougscore.presentation.compare

import gabor.koleszar.dougscore.domain.model.Car

data class CompareResultState(
    val car1: Car? = null,
    val car2: Car? = null,
    val isLoading: Boolean = true,
)
