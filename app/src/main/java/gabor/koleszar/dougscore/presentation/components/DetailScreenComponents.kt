package gabor.koleszar.dougscore.presentation.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import gabor.koleszar.dougscore.R
import gabor.koleszar.dougscore.domain.model.Car
import gabor.koleszar.dougscore.presentation.StyleConstants
import gabor.koleszar.dougscore.presentation.StyleConstants.SPACER_WIDTH
import gabor.koleszar.dougscore.presentation.theme.ScoreGreen
import gabor.koleszar.dougscore.presentation.theme.ScoreRed

@Composable
fun DougScoreTable(
	car: Car,
	modifier: Modifier = Modifier
) {
	Row(
		modifier
			.fillMaxWidth()
			.padding(vertical = SPACER_WIDTH),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center
	) {
		Column(
			Modifier.weight(0.3f),
			horizontalAlignment = Alignment.End
		) {
			OneLineText(text = stringResource(id = R.string.details_car_daily_value))
			OneLineText(text = stringResource(id = R.string.details_car_daily_comfort))
			OneLineText(text = stringResource(id = R.string.details_car_daily_features))
			OneLineText(text = stringResource(id = R.string.details_car_daily_practicality))
			OneLineText(text = stringResource(id = R.string.details_car_daily_quality))
			OneLineText(text = stringResource(id = R.string.details_car_daily_total))
		}
		Column(
			Modifier.weight(0.2f),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			SubScoreColoredTextField(score = car.dailyScore.value)
			SubScoreColoredTextField(score = car.dailyScore.comfort)
			SubScoreColoredTextField(score = car.dailyScore.features)
			SubScoreColoredTextField(score = car.dailyScore.practicality)
			SubScoreColoredTextField(score = car.dailyScore.quality)
			Text(
				text = car.dailyScore.total.toString(),
				color = StyleConstants.getColorFromScore(car.dailyScore.total)
			)
		}
		Column(
			Modifier.weight(0.3f),
			horizontalAlignment = Alignment.End
		) {
			OneLineText(text = stringResource(id = R.string.details_car_weekend_acceleration))
			OneLineText(text = stringResource(id = R.string.details_car_weekend_styling))
			OneLineText(text = stringResource(id = R.string.details_car_weekend_handling))
			OneLineText(text = stringResource(id = R.string.details_car_weekend_fun_factor))
			OneLineText(text = stringResource(id = R.string.details_car_weekend_cool_factor))
			OneLineText(text = stringResource(id = R.string.details_car_weekend_total))
		}
		Column(
			Modifier.weight(0.2f),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			SubScoreColoredTextField(score = car.weekendScore.acceleration)
			SubScoreColoredTextField(score = car.weekendScore.styling)
			SubScoreColoredTextField(score = car.weekendScore.handling)
			SubScoreColoredTextField(score = car.weekendScore.funFactor)
			SubScoreColoredTextField(score = car.weekendScore.coolFactor)
			Text(
				text = car.weekendScore.total.toString(),
				color = StyleConstants.getColorFromScore(car.weekendScore.total)
			)
		}
	}
}

@Composable
fun SubScoreColoredTextField(
	score: Byte,
	modifier: Modifier = Modifier
) {
	Text(
		text = score.toString(),
		modifier,
		color = StyleConstants.getColorFromSubScore(score)
	)
}

@Composable
fun OneLineText(
	text: String,
	modifier: Modifier = Modifier
) {
	Text(text = text, modifier = modifier.basicMarquee(), maxLines = 1)
}

@Composable
private fun compareColor(mine: Int, theirs: Int): Color = when {
	mine > theirs -> ScoreGreen
	mine < theirs -> ScoreRed
	else -> MaterialTheme.colorScheme.onSurface
}

@Composable
fun CompareScoreTable(
	thisCar: Car,
	otherCar: Car,
	modifier: Modifier = Modifier
) {
	Row(
		modifier
			.fillMaxWidth()
			.padding(vertical = SPACER_WIDTH),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center
	) {
		Column(
			Modifier.weight(0.3f),
			horizontalAlignment = Alignment.End
		) {
			OneLineText(text = stringResource(id = R.string.details_car_daily_value))
			OneLineText(text = stringResource(id = R.string.details_car_daily_comfort))
			OneLineText(text = stringResource(id = R.string.details_car_daily_features))
			OneLineText(text = stringResource(id = R.string.details_car_daily_practicality))
			OneLineText(text = stringResource(id = R.string.details_car_daily_quality))
			OneLineText(text = stringResource(id = R.string.details_car_daily_total))
		}
		Column(
			Modifier.weight(0.2f),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(
				text = thisCar.dailyScore.value.toString(),
				color = compareColor(thisCar.dailyScore.value.toInt(), otherCar.dailyScore.value.toInt())
			)
			Text(
				text = thisCar.dailyScore.comfort.toString(),
				color = compareColor(thisCar.dailyScore.comfort.toInt(), otherCar.dailyScore.comfort.toInt())
			)
			Text(
				text = thisCar.dailyScore.features.toString(),
				color = compareColor(thisCar.dailyScore.features.toInt(), otherCar.dailyScore.features.toInt())
			)
			Text(
				text = thisCar.dailyScore.practicality.toString(),
				color = compareColor(thisCar.dailyScore.practicality.toInt(), otherCar.dailyScore.practicality.toInt())
			)
			Text(
				text = thisCar.dailyScore.quality.toString(),
				color = compareColor(thisCar.dailyScore.quality.toInt(), otherCar.dailyScore.quality.toInt())
			)
			Text(
				text = thisCar.dailyScore.total.toString(),
				color = compareColor(thisCar.dailyScore.total.toInt(), otherCar.dailyScore.total.toInt())
			)
		}
		Column(
			Modifier.weight(0.3f),
			horizontalAlignment = Alignment.End
		) {
			OneLineText(text = stringResource(id = R.string.details_car_weekend_acceleration))
			OneLineText(text = stringResource(id = R.string.details_car_weekend_styling))
			OneLineText(text = stringResource(id = R.string.details_car_weekend_handling))
			OneLineText(text = stringResource(id = R.string.details_car_weekend_fun_factor))
			OneLineText(text = stringResource(id = R.string.details_car_weekend_cool_factor))
			OneLineText(text = stringResource(id = R.string.details_car_weekend_total))
		}
		Column(
			Modifier.weight(0.2f),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(
				text = thisCar.weekendScore.acceleration.toString(),
				color = compareColor(thisCar.weekendScore.acceleration.toInt(), otherCar.weekendScore.acceleration.toInt())
			)
			Text(
				text = thisCar.weekendScore.styling.toString(),
				color = compareColor(thisCar.weekendScore.styling.toInt(), otherCar.weekendScore.styling.toInt())
			)
			Text(
				text = thisCar.weekendScore.handling.toString(),
				color = compareColor(thisCar.weekendScore.handling.toInt(), otherCar.weekendScore.handling.toInt())
			)
			Text(
				text = thisCar.weekendScore.funFactor.toString(),
				color = compareColor(thisCar.weekendScore.funFactor.toInt(), otherCar.weekendScore.funFactor.toInt())
			)
			Text(
				text = thisCar.weekendScore.coolFactor.toString(),
				color = compareColor(thisCar.weekendScore.coolFactor.toInt(), otherCar.weekendScore.coolFactor.toInt())
			)
			Text(
				text = thisCar.weekendScore.total.toString(),
				color = compareColor(thisCar.weekendScore.total.toInt(), otherCar.weekendScore.total.toInt())
			)
		}
	}
	Row(
		modifier
			.fillMaxWidth()
			.padding(vertical = SPACER_WIDTH),
		horizontalArrangement = Arrangement.Center
	) {
		Text(text = stringResource(id = R.string.compare_dougscore_label) + ": ", fontWeight = FontWeight.Bold)
		Text(
			text = thisCar.dougScore.toString(),
			color = compareColor(thisCar.dougScore, otherCar.dougScore),
			fontWeight = FontWeight.Bold
		)
	}
}