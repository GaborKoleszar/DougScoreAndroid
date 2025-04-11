package gabor.koleszar.dougscore.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import gabor.koleszar.dougscore.data.local.entity.CarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {

	@Upsert
	suspend fun insert(carIntroductions: List<CarEntity>)

	@Query("SELECT * FROM cars")
	fun getAllCars(): Flow<List<CarEntity>>

	@Query("DELETE FROM cars")
	suspend fun deleteAllCars()

	@Query("SELECT * FROM cars WHERE id = :id LIMIT 1")
	fun getCarWithId(id: Int): Flow<CarEntity>
}