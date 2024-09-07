package gabor.koleszar.dougscore.data.repository

import gabor.koleszar.dougscore.common.Resource
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
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
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

	override fun getAllCars(
		shouldFetchFromRemote: Boolean
	): Flow<Resource<List<Car>>> {

		return flow {
			emit(Resource.Loading())

			val localCars = dao.getAllCars()
			val cacheExists = localCars.isNotEmpty()

			if (cacheExists && !shouldFetchFromRemote) {
				emit(Resource.Success(
					data = localCars.map { carEntity ->
						carEntity.toDomainModel()
					}
				))
				return@flow
			}

			val remoteCars = try {
				val response = api.getDougScoreExcelFile()
				carDataParser.parse(response.byteStream())
			} catch (e: IOException) {
				emit(
					Resource.Error(
						message = e.localizedMessage ?: e.toString(),
						data = null
					)
				)
				null
			} catch (e: HttpException) {
				emit(
					Resource.Error(
						message = e.localizedMessage ?: e.toString(),
						data = null
					)
				)
				null
			}

			remoteCars?.let { cars ->
				dao.deleteAllCars()
				dao.insert(
					cars.map {
						it.toEntity()
					}
				)
				emit(Resource.Success(
					data = dao.getAllCars().map { it.toDomainModel() }
				))
				userPreferencesRepository.saveLastTimeDataUpdated(System.currentTimeMillis())
			}
		}
	}

	override fun getCarWithId(id: Int): Flow<Resource<Car>> {
		return flow {
			emit(Resource.Loading())
			try {
				val car = dao.getCarWithId(id).toDomainModel()
				emit(Resource.Success(car))
			} catch (e: Exception) {
				emit(
					Resource.Error("Getting car $id failed")
				)
			}
		}
	}

	override suspend fun setCars(cars: List<CarDto>) {
		dao.deleteAllCars()
		dao.insert(cars.map { car ->
			car.toEntity()
		})
	}
}