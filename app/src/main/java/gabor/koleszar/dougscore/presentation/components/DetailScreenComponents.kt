package gabor.koleszar.dougscore.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import gabor.koleszar.dougscore.R
import gabor.koleszar.dougscore.domain.model.Car
import gabor.koleszar.dougscore.presentation.StyleConstants.SPACER_WIDTH

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
			Text(text = car.dailyScore.value.toString())
			Text(text = car.dailyScore.comfort.toString())
			Text(text = car.dailyScore.features.toString())
			Text(text = car.dailyScore.practicality.toString())
			Text(text = car.dailyScore.quality.toString())
			Text(text = car.dailyScore.total.toString())
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
			Text(text = car.weekendScore.acceleration.toString())
			Text(text = car.weekendScore.styling.toString())
			Text(text = car.weekendScore.handling.toString())
			Text(text = car.weekendScore.funFactor.toString())
			Text(text = car.weekendScore.coolFactor.toString())
			Text(text = car.weekendScore.total.toString())
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OneLineText(
	text: String,
	modifier: Modifier = Modifier
) {
	Text(text = text, modifier = modifier.basicMarquee(), maxLines = 1, )
}