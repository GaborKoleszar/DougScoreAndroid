package gabor.koleszar.dougscore.data.mapper

import gabor.koleszar.dougscore.data.local.entity.CarEntity
import gabor.koleszar.dougscore.data.local.entity.DailyScoreEntity
import gabor.koleszar.dougscore.data.local.entity.WeekendScoreEntity
import gabor.koleszar.dougscore.domain.model.Car
import gabor.koleszar.dougscore.domain.model.DailyScore
import gabor.koleszar.dougscore.domain.model.WeekendScore

fun CarEntity.toDomainModel(): Car {
	return Car(
		year = year,
		manufacturer = manufacturer,
		model = model,
		weekendScore = WeekendScore(
			styling = weekendScore.styling,
			acceleration = weekendScore.acceleration,
			handling = weekendScore.handling,
			funFactor = weekendScore.funFactor,
			coolFactor = weekendScore.coolFactor,
			total = weekendScore.total
		),
		dailyScore = DailyScore(
			features = dailyScore.features,
			comfort = dailyScore.comfort,
			quality = dailyScore.quality,
			practicality = dailyScore.practicality,
			value = dailyScore.value,
			total = dailyScore.total
		),
		dougScore = dougScore,
		videoLink = videoLink,
		imageLink = imageLink,
		filmingLocationCity = filmingLocationCity,
		filmingLocationState = filmingLocationState,
		vehicleCountry = vehicleCountry
	)
}

fun Car.toEntity(): CarEntity {
	return CarEntity(
		year = year,
		manufacturer = manufacturer,
		model = model,
		weekendScore = WeekendScoreEntity(
			styling = weekendScore.styling,
			acceleration = weekendScore.acceleration,
			handling = weekendScore.handling,
			funFactor = weekendScore.funFactor,
			coolFactor = weekendScore.coolFactor,
			total = weekendScore.total
		),
		dailyScore = DailyScoreEntity(
			features = dailyScore.features,
			comfort = dailyScore.comfort,
			quality = dailyScore.quality,
			practicality = dailyScore.practicality,
			value = dailyScore.value,
			total = dailyScore.total
		),
		dougScore = dougScore,
		videoLink = videoLink,
		imageLink = imageLink,
		filmingLocationCity = filmingLocationCity,
		filmingLocationState = filmingLocationState,
		vehicleCountry = vehicleCountry
	)
}