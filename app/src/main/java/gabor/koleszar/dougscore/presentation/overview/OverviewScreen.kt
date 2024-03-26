package gabor.koleszar.dougscore.presentation.overview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gabor.koleszar.dougscore.R
import gabor.koleszar.dougscore.common.Constants.YT_IMAGE_FALLBACK_URL
import gabor.koleszar.dougscore.common.Constants.YT_IMAGE_HQDEFAULT
import gabor.koleszar.dougscore.common.Constants.YT_IMAGE_SDDEFAULT
import gabor.koleszar.dougscore.common.Constants.YT_IMAGE_URL
import gabor.koleszar.dougscore.domain.model.Car
import gabor.koleszar.dougscore.domain.model.DailyScore
import gabor.koleszar.dougscore.domain.model.WeekendScore
import gabor.koleszar.dougscore.presentation.StyleConstants.BORDER_RADIUS
import gabor.koleszar.dougscore.presentation.StyleConstants.DEFAULT_PADDING
import gabor.koleszar.dougscore.presentation.StyleConstants.SPACER_WIDTH
import gabor.koleszar.dougscore.presentation.components.AsyncImageWithFallbackUrl
import gabor.koleszar.dougscore.presentation.theme.Bronze
import gabor.koleszar.dougscore.presentation.theme.DougScoreTheme
import gabor.koleszar.dougscore.presentation.theme.Gold
import gabor.koleszar.dougscore.presentation.theme.Silver

@Composable
fun OverviewScreen(
	onCarClick: (Int) -> Unit,
	cars: List<Car>,
	modifier: Modifier = Modifier
) {
	if (cars.isEmpty()) {
		InitialListView(modifier)
	} else {
		LoadedListView(
			onCarClick,
			cars,
			modifier
		)
	}
}

@Composable
fun LoadedListView(
	onCarClick: (Int) -> Unit,
	cars: List<Car>,
	modifier: Modifier = Modifier
) {
	Box(
		modifier = Modifier
			.fillMaxSize()
	) {
		LazyColumn(
			modifier = modifier
				.fillMaxSize()
				.padding(horizontal = SPACER_WIDTH)
		) {
			itemsIndexed(cars) { index, car ->
				CarListItem(car, { onCarClick(index) })
			}
		}
	}
}

@Composable
fun InitialListView(modifier: Modifier = Modifier) {
	Column(
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier
			.fillMaxSize()
	) {
		Text(text = stringResource(R.string.overview_scree_loading_data_please_wait))
		Spacer(modifier = Modifier.height(SPACER_WIDTH))
		CircularProgressIndicator()
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CarListItem(
	car: Car, onCarClick: () -> Unit, modifier: Modifier = Modifier
) {
	Spacer(
		modifier = modifier
			.fillMaxWidth()
			.height(SPACER_WIDTH / 2)
	)
	Card(
		shape = RoundedCornerShape(BORDER_RADIUS),
		onClick = onCarClick,
		modifier = modifier.fillMaxWidth(),
		elevation = CardDefaults.cardElevation(
			defaultElevation = 5.dp
		)
	) {
		Row(
			modifier = Modifier
				.height(110.dp)
				.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Spacer(modifier = Modifier.width(SPACER_WIDTH))
			Text(
				fontStyle = FontStyle.Italic,
				text = "#${car.id + 1}",
				modifier = Modifier
					.vertical()
					.rotate(-90.0f)
					.height(30.dp),
				color = when (car.id + 1) {
					1 -> Gold
					2 -> Silver
					3 -> Bronze
					else -> MaterialTheme.colorScheme.onSurface
				},
				textAlign = TextAlign.Center
			)
			Column(
				modifier = Modifier
					.padding(vertical = DEFAULT_PADDING)
					.weight(0.41f),
				horizontalAlignment = Alignment.Start,
				verticalArrangement = Arrangement.SpaceBetween
			) {
				Text(
					fontWeight = FontWeight.Bold,
					text = car.manufacturer,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis
				)
				Text(
					fontWeight = FontWeight.Bold,
					text = car.model,
					maxLines = 1,
					modifier = Modifier.basicMarquee()
				)
				Text(text = "Dougscore: " + car.dougScore)
			}
			Spacer(modifier = Modifier.width(SPACER_WIDTH))
			AsyncImageWithFallbackUrl(
				model = YT_IMAGE_URL + car.imageLink + YT_IMAGE_SDDEFAULT,
				fallbackModel = YT_IMAGE_FALLBACK_URL + car.imageLink + YT_IMAGE_HQDEFAULT,
				modifier = Modifier
					.width(200.dp)
					.graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
					.drawWithContent {
						drawContent()
						drawRect(
							brush = Brush.horizontalGradient(
								0.0f to Color.Transparent, 0.1f to Color.Black
							), blendMode = BlendMode.DstIn
						)
					}
			)
		}
	}
	Spacer(
		modifier = modifier
			.fillMaxWidth()
			.height(SPACER_WIDTH / 2)
	)
}

fun Modifier.vertical() = layout { measurable, constraints ->
	val placeable = measurable.measure(constraints)
	layout(placeable.height, placeable.width) {
		placeable.place(
			x = -(placeable.width / 2 - placeable.height / 2),
			y = -(placeable.height / 2 - placeable.width / 2)
		)
	}
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
		dummyCars().first()
	}
	DougScoreTheme {
		CarListItem(car, {})
	}
}

@Preview(
	showBackground = true, heightDp = 500, widthDp = 380
)
@Composable
fun CarListPreview() {
	DougScoreTheme {
		OverviewScreen(
			onCarClick = {},
			cars = dummyCars()
		)
	}
}

private fun dummyCars(): List<Car> {
	val cars = mutableListOf<Car>()
	for (i in 0..5) {
		cars.add(
			Car(
				i,
				1992,
				"Honda",
				"Civic",
				WeekendScore(
					6, 5, 8, 9, 4, 45
				),
				DailyScore(
					4, 7, 5, 3, 2, 34
				),
				85,
				"https://www.youtube.com/watch?v=_UKBxM7m7qo",
				"https://i.ytimg.com/vi/UKBxM7m7qo/maxresdefault.jpg",
				"Los Angeles",
				"California",
				"Japan"
			)
		)
	}
	return cars
}
