package gabor.koleszar.dougscore.domain.repository

import gabor.koleszar.dougscore.common.Resource
import gabor.koleszar.dougscore.data.dto.CarDto
import gabor.koleszar.dougscore.domain.model.Car
import kotlinx.coroutines.flow.Flow

interface CarRepository {

	suspend fun getAllCars(
		shouldFetchFromRemote: Boolean
	): Flow<Resource<List<Car>>>

	suspend fun getCarWithId(id: Int): Flow<Resource<Car>>
	suspend fun setCars(cars: List<CarDto>)
}