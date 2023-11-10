package gabor.koleszar.dougscore.presentation.overview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import gabor.koleszar.dougscore.domain.model.Car
import gabor.koleszar.dougscore.domain.model.DailyScore
import gabor.koleszar.dougscore.domain.model.WeekendScore
import gabor.koleszar.dougscore.presentation.StyleConstants.BORDER_RADIUS
import gabor.koleszar.dougscore.presentation.StyleConstants.DEFAULT_PADDING
import gabor.koleszar.dougscore.presentation.StyleConstants.SPACER_WIDTH
import gabor.koleszar.dougscore.presentation.theme.DougScoreTheme

@Composable
fun OverviewScreen(
	onCarClick: () -> Unit,
	overviewState: OverviewState,
	modifier: Modifier = Modifier
) {
	if (overviewState.isLoading) {
		Box(
			modifier = modifier.fillMaxSize(),
			contentAlignment = Alignment.Center
		) {
			Column(horizontalAlignment = Alignment.CenterHorizontally) {
				Text(text = "First time parsing of table.")
				Text(text = "Subsequent opens will be faster.")
				Spacer(modifier = Modifier.height(SPACER_WIDTH))
				CircularProgressIndicator()
			}
		}
	} else {
		LazyColumn(modifier = modifier.fillMaxSize()) {
			items(overviewState.cars) { car ->
				CarListItem(car, onCarClick)
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarListItem(
	car: Car,
	onCarClick: () -> Unit,
	modifier: Modifier = Modifier
) {
	Card(
		shape = RoundedCornerShape(BORDER_RADIUS),
		onClick = onCarClick,
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = SPACER_WIDTH)
	) {
		Row(modifier = Modifier.padding(DEFAULT_PADDING)) {
			Text(text = car.manufacturer)
			Spacer(modifier = Modifier.width(SPACER_WIDTH))
			Text(text = car.model)
			Spacer(modifier = Modifier.width(SPACER_WIDTH))
			car.videoLink?.let { Text(text = it) }
		}
	}
	Spacer(
		modifier = modifier
			.fillMaxWidth()
			.height(SPACER_WIDTH)
	)
}
/*
* * * * * * * * * *
*  PREVIEW BELOW  *
* * * * * * * * * *
*/

@Preview(
	showBackground = false
)
@Composable
fun CarListItemPreview() {
	val car = remember {
		Car(
			1992,
			"Honda",
			"Civic",
			WeekendScore(
				6,
				5,
				8,
				9,
				4,
				45
			),
			DailyScore(
				4,
				7,
				5,
				3,
				2,
				34
			),
			85,
			"THIS IS A URL",
			"Los Angeles",
			"California",
			"Japan"
		)
	}
	DougScoreTheme {
		CarListItem(car, {})
	}
}
