package gabor.koleszar.dougscore.data.repository

import gabor.koleszar.dougscore.data.dto.CarDto
import gabor.koleszar.dougscore.data.local.CarDatabase
import gabor.koleszar.dougscore.data.mapper.toDomainModel
import gabor.koleszar.dougscore.data.mapper.toEntity
import gabor.koleszar.dougscore.data.remote.CarDataParser
import gabor.koleszar.dougscore.data.remote.DougScoreApi
import gabor.koleszar.dougscore.domain.model.Car
import gabor.koleszar.dougscore.domain.repository.CarRepository
import gabor.koleszar.dougscore.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarRepositoryImpl @Inject constructor(
	database: CarDatabase,
	private val api: DougScoreApi,
	private val carDataParser: CarDataParser<CarDto>,
	private val userPreferencesRepository: UserPreferencesRepository
) : CarRepository {

	private val dao = database.carDao

	override suspend fun downloadCars(): Boolean {
		val remoteCars = try {
			val response = api.getDougScoreExcelFile()
			carDataParser.parse(response.byteStream())
		} catch (e: IOException) {
			return false
		} catch (e: HttpException) {
			return false
		}
		dao.insert(remoteCars.map { carDto ->
			carDto.toEntity()
		})
		userPreferencesRepository.saveLastTimeDataUpdated(System.currentTimeMillis())
		return true
	}

	override fun getCars(): Flow<List<Car>> = dao.getAllCars()
		.onStart {
			val isEmpty = dao.getAllCars().first().isEmpty()
			val lastUpdated = userPreferencesRepository.loadLastTimeDataUpdated().first()
			val isStale = System.currentTimeMillis() - lastUpdated > 7 * 24 * 60 * 60 * 1000L
			if (isEmpty || isStale) {
				downloadCars()
			}
		}
		.distinctUntilChanged()
		.map { carEntities ->
			carEntities.map { carEntity ->
				carEntity.toDomainModel()
			}
		}

	override fun getCarWithId(id: Int): Flow<Car> = dao.getCarWithId(id)
		.distinctUntilChanged()
		.map { carEntity ->
			carEntity.toDomainModel()
		}

	override suspend fun setCars(cars: List<CarDto>) {
		dao.deleteAllCars()
		dao.insert(cars.map { car ->
			car.toEntity()
		})
	}
}