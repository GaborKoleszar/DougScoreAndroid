package gabor.koleszar.dougscore.presentation.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import gabor.koleszar.dougscore.common.Constants.YT_IMAGE_HQDEFAULT
import gabor.koleszar.dougscore.domain.model.Car
import gabor.koleszar.dougscore.domain.model.DailyScore
import gabor.koleszar.dougscore.domain.model.WeekendScore
import gabor.koleszar.dougscore.presentation.StyleConstants.BORDER_RADIUS
import gabor.koleszar.dougscore.presentation.StyleConstants.DEFAULT_PADDING
import gabor.koleszar.dougscore.presentation.StyleConstants.SPACER_WIDTH
import gabor.koleszar.dougscore.presentation.theme.DougScoreTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OverviewScreen(
	onCarClick: (Int) -> Unit,
	onPullRefresh: () -> Unit,
	overviewState: OverviewState,
	modifier: Modifier = Modifier
) {
	if (overviewState.cars.isEmpty()) {
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
		val pullRefreshState = rememberPullRefreshState(overviewState.isRefreshing, onPullRefresh)
		Box(
			modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
		) {
			LazyColumn(
				modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = SPACER_WIDTH)
			) {
				item {
					TextField(
						value = "",
						onValueChange = { },
						placeholder = { Text("Search") },
						leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
						modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = SPACER_WIDTH)
					)
				}
				itemsIndexed(overviewState.cars) { index, car ->
					CarListItem(car, { onCarClick(index) })
				}
			}
			PullRefreshIndicator(
				overviewState.isRefreshing,
				pullRefreshState,
				Modifier.align(Alignment.TopCenter)
			)
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
		colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
		shape = RoundedCornerShape(BORDER_RADIUS),
		onClick = onCarClick,
		modifier = modifier
			.fillMaxWidth(),
		elevation = CardDefaults.cardElevation(
			defaultElevation = 5.dp
		)
	) {
		Row(
			modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				fontStyle = FontStyle.Italic,
				text = " #${car.id + 1}"
			)
			Column(
				modifier = Modifier
                    .padding(DEFAULT_PADDING)
                    .weight(0.42f),
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
					overflow = TextOverflow.Ellipsis
				)
				Text(text = "Dougscore: " + car.dougScore)
			}
			car.imageLink?.let {
				AsyncImage(
					model = ImageRequest.Builder(LocalContext.current)
						.data(car.imageLink + YT_IMAGE_HQDEFAULT)
						.crossfade(true)
						.build(),
					contentDescription = null,
					modifier = Modifier
                        .weight(0.53f)
                        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                        .drawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.horizontalGradient(
                                    0.0f to Color.Transparent,
                                    0.3f to Color.Black
                                ),
                                blendMode = BlendMode.DstIn
                            )
                        },
					contentScale = ContentScale.Crop
				)
			}
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
		dummyCars().first()
	}
	DougScoreTheme {
		CarListItem(car, {})
	}
}

@Preview(
	showBackground = true,
	heightDp = 500,
	widthDp = 380
)
@Composable
fun CarListPreview() {
	DougScoreTheme {
		OverviewScreen(
			onCarClick = {},
			overviewState = OverviewState(dummyCars()),
			onPullRefresh = {}
		)
	}
}

private fun dummyCars(): List<Car> {
	val cars = mutableListOf<Car>()
	for (i in 1..5) {
		cars.add(
			Car(
				i,
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
