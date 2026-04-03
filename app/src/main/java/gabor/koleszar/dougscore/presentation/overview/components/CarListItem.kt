package gabor.koleszar.dougscore.presentation.overview.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gabor.koleszar.dougscore.domain.model.Car
import gabor.koleszar.dougscore.domain.model.DailyScore
import gabor.koleszar.dougscore.domain.model.WeekendScore
import gabor.koleszar.dougscore.presentation.StyleConstants
import gabor.koleszar.dougscore.presentation.StyleConstants.DEFAULT_PADDING
import gabor.koleszar.dougscore.presentation.StyleConstants.SPACER_WIDTH
import gabor.koleszar.dougscore.presentation.components.AsyncImageWithMultipleFallback
import gabor.koleszar.dougscore.presentation.theme.Bronze
import gabor.koleszar.dougscore.presentation.theme.DougScoreTheme
import gabor.koleszar.dougscore.presentation.theme.Gold
import gabor.koleszar.dougscore.presentation.theme.Silver

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CarListItem(
	car: Car,
	onCarClick: () -> Unit,
	animatedVisibilityScope: AnimatedVisibilityScope,
	modifier: Modifier = Modifier.widthIn(300.dp, 600.dp)
) {
	Spacer(
		modifier = modifier
			.fillMaxWidth()
			.height(SPACER_WIDTH)
	)
	Box(
		modifier = modifier
			.fillMaxWidth()
			.clickable(onClick = onCarClick),
	) {
		Row(
			modifier = Modifier
				.height(110.dp)
				.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			AsyncImageWithMultipleFallback(
				model = car.getMaxresImageLink(),
				fallbackModel = car.getHqFallbackImageLink(),
				modifier = Modifier
					.sharedElement(
						sharedContentState = rememberSharedContentState(key = "car_image_${car.id}"),
						animatedVisibilityScope = animatedVisibilityScope,
					)
					.width(200.dp)
					.clip(RoundedCornerShape(DEFAULT_PADDING))
			)
			Box(modifier = Modifier.fillMaxSize()) {
				Card(
					shape = RoundedCornerShape(
						0.dp,
						DEFAULT_PADDING,
						DEFAULT_PADDING,
						0.dp
					),
					colors = CardDefaults.cardColors(
						containerColor = CardDefaults.cardColors().containerColor.copy(alpha = 0.4f)
					),
					modifier = Modifier
						.fillMaxSize()
						.graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
						.drawWithContent {
							drawContent()
							drawRect(
								brush = Brush.horizontalGradient(
									0.0f to Color.Transparent, 1.0f to Color.Black
								), blendMode = BlendMode.DstIn
							)
						},
					elevation = CardDefaults.cardElevation(StyleConstants.ZERO_ELEVATION)
				) {
				}
				Column(
					modifier = Modifier
						.padding(DEFAULT_PADDING)
						.fillMaxHeight(),
					horizontalAlignment = Alignment.Start,
					verticalArrangement = Arrangement.SpaceBetween
				) {
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Text(
							fontWeight = FontWeight.Bold,
							text = car.manufacturer,
							maxLines = 1,
							overflow = TextOverflow.Ellipsis,
							modifier = Modifier
								.sharedBounds(
									sharedContentState = rememberSharedContentState(key = "car_manufacturer_${car.id}"),
									animatedVisibilityScope = animatedVisibilityScope,
								)
								.basicMarquee()
								.weight(0.9f)
						)
						Spacer(modifier = Modifier.width(SPACER_WIDTH))
						Text(
							fontStyle = FontStyle.Italic,
							text = "#${car.id + 1}",
							color = when (car.id + 1) {
								1 -> Gold
								2 -> Silver
								3 -> Bronze
								else -> MaterialTheme.colorScheme.onSurface
							},
							textAlign = TextAlign.Center,
							maxLines = 1,
							modifier = Modifier
								.sharedBounds(
									sharedContentState = rememberSharedContentState(key = "car_rank_${car.id}"),
									animatedVisibilityScope = animatedVisibilityScope,
								)
						)
					}
					Text(
						fontWeight = FontWeight.SemiBold,
						text = car.model,
						maxLines = 1,
						modifier = Modifier
							.sharedBounds(
								sharedContentState = rememberSharedContentState(key = "car_model_${car.id}"),
								animatedVisibilityScope = animatedVisibilityScope,
							)
							.basicMarquee()
					)
					Text(
						text = "Score: " + car.dougScore,
						modifier = Modifier
							.sharedBounds(
								sharedContentState = rememberSharedContentState(key = "car_dougscore_${car.id}"),
								animatedVisibilityScope = animatedVisibilityScope,
							)
					)
				}
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
		//CarListItem(car, {})
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