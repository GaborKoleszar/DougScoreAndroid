package gabor.koleszar.dougscore.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class CarEntity(
	val year: Int,
	val manufacturer: String,
	val model: String,
	@Embedded(prefix = "weekend_score_") val weekendScore: WeekendScoreEntity,
	@Embedded(prefix = "daily_score_") val dailyScore: DailyScoreEntity,
	val dougScore: Int,
	val videoLink: String?,
	val imageLink: String?,
	val filmingLocationCity: String,
	val filmingLocationState: String,
	val vehicleCountry: String,
	@PrimaryKey(autoGenerate = true) val id: Int = 0
)