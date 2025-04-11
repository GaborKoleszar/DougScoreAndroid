package gabor.koleszar.dougscore.domain.repository

import gabor.koleszar.dougscore.data.dto.CarDto
import gabor.koleszar.dougscore.domain.model.Car
import kotlinx.coroutines.flow.Flow

interface CarRepository {

	suspend fun downloadCars(): Boolean

	fun getCars(): Flow<List<Car>>

	fun getCarWithId(id: Int): Flow<Car>

	suspend fun setCars(cars: List<CarDto>)
}