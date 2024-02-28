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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import gabor.koleszar.dougscore.presentation.theme.Bronze
import gabor.koleszar.dougscore.presentation.theme.DougScoreTheme
import gabor.koleszar.dougscore.presentation.theme.Gold
import gabor.koleszar.dougscore.presentation.theme.Silver

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OverviewScreen(
	onCarClick: (Int) -> Unit,
	onPullRefresh: () -> Unit,
	onSearchTextChanged: (String) -> Unit,
	cars: List<Car>,
	isLoading: Boolean,
	isRefreshing: Boolean,
	searchText: String,
	modifier: Modifier = Modifier
) {
	if (cars.isEmpty()) {
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
		val pullRefreshState = rememberPullRefreshState(isRefreshing, onPullRefresh)
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
						value = searchText,
						onValueChange = onSearchTextChanged,
						placeholder = { Text("Search") },
						leadingIcon = {
							Icon(
								Icons.Default.Search,
								contentDescription = null,
								tint = MaterialTheme.colorScheme.onSurface
							)
						},
						shape = RoundedCornerShape(BORDER_RADIUS),
						maxLines = 1,
						singleLine = true,
						modifier = Modifier
							.fillMaxWidth()
							.padding(bottom = SPACER_WIDTH),
						colors = TextFieldDefaults.colors(
							focusedIndicatorColor = Color.Transparent,
							unfocusedIndicatorColor = Color.Transparent,
							disabledIndicatorColor = Color.Transparent,
						)
					)
				}
				itemsIndexed(cars) { index, car ->
					CarListItem(car, { onCarClick(index) })
				}
			}
			PullRefreshIndicator(
				isRefreshing,
				pullRefreshState,
				Modifier.align(Alignment.TopCenter)
			)
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
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
			defaultElevation = 10.dp
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
			car.imageLink?.let {
				AsyncImage(
					model = ImageRequest.Builder(LocalContext.current)
						.data(car.imageLink + YT_IMAGE_HQDEFAULT)
						.crossfade(true)
						.build(),
					contentDescription = null,
					modifier = Modifier
						.width(200.dp)
						.graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
						.drawWithContent {
							drawContent()
							drawRect(
								brush = Brush.horizontalGradient(
									0.0f to Color.Transparent,
									0.1f to Color.Black
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

fun Modifier.vertical() =
	layout { measurable, constraints ->
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
	showBackground = true,
	heightDp = 500,
	widthDp = 380
)
@Composable
fun CarListPreview() {
	DougScoreTheme {
		OverviewScreen(
			onCarClick = {},
			onPullRefresh = {},
			onSearchTextChanged = {},
			cars = dummyCars(),
			isLoading = false,
			isRefreshing = false,
			searchText = ""
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
