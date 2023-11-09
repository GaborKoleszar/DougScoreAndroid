package gabor.koleszar.dougscore.domain.model

data class DailyScore(
	val features: Byte,
	val comfort: Byte,
	val quality: Byte,
	val practicality: Byte,
	val value: Byte,
	val total: Byte
)