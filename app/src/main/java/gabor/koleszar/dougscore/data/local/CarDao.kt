package gabor.koleszar.dougscore.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import gabor.koleszar.dougscore.data.local.entity.CarEntity

@Dao
interface CarDao {

	@Upsert
	suspend fun insert(carIntroductions: List<CarEntity>)

	@Query("SELECT * FROM cars")
	suspend fun getAllCars(): List<CarEntity>

	@Query("DELETE FROM cars")
	suspend fun deleteAllCars()
}