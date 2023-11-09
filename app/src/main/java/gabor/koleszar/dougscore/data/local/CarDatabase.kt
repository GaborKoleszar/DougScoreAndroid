package gabor.koleszar.dougscore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import gabor.koleszar.dougscore.data.local.entity.CarEntity

@Database(
	entities = [CarEntity::class],
	version = 1,
	exportSchema = false
)
abstract class CarDatabase : RoomDatabase() {
	abstract val carDao: CarDao
}