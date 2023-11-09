package gabor.koleszar.dougscore.data.local.entity

data class DailyScoreEntity(
	val features: Byte,
	val comfort: Byte,
	val quality: Byte,
	val practicality: Byte,
	val value: Byte,
	val total: Byte
)