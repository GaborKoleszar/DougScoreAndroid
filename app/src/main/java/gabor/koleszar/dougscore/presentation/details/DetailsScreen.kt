@file:OptIn(ExperimentalSharedTransitionApi::class)

package gabor.koleszar.dougscore.presentation.details

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gabor.koleszar.dougscore.presentation.StyleConstants.DEFAULT_PADDING
import gabor.koleszar.dougscore.presentation.StyleConstants.SPACER_WIDTH
import gabor.koleszar.dougscore.presentation.components.AsyncImageWithMultipleFallback
import gabor.koleszar.dougscore.presentation.components.DougScoreTable

@Composable
fun SharedTransitionScope.DetailsScreen(
	modifier: Modifier = Modifier,
	carId: Int,
	detailsViewModel: DetailsViewModel = hiltViewModel(),
	animatedVisibilityScope: AnimatedVisibilityScope
) {
	LaunchedEffect(carId) {
		detailsViewModel.setCarInDetails(carId)
	}
	val car by detailsViewModel.carInDetailsScreen.collectAsStateWithLifecycle()
	Box(
		modifier = modifier
			.fillMaxWidth()
			.padding(DEFAULT_PADDING)
			.verticalScroll(rememberScrollState())
	) {
		val context = LocalContext.current
		Column(
			modifier.fillMaxWidth()
		) {
			if (car != null) {
				val notNullCar = car!!
				AsyncImageWithMultipleFallback(
					model = notNullCar.getMaxresImageLink(),
					fallbackModel = notNullCar.getHqFallbackImageLink(),
					modifier = Modifier
						.sharedElement(
							state = rememberSharedContentState(key = "car_image_${notNullCar.id}"),
							animatedVisibilityScope = animatedVisibilityScope,
						)
						.fillMaxWidth()
						.clip(RoundedCornerShape(DEFAULT_PADDING)),
					contentScale = ContentScale.FillWidth
				)
				Column(
					modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
				) {
					//Basic info
					Spacer(modifier = Modifier.height(DEFAULT_PADDING))
					Row {
						Text(text = "Manufacturer : ")
						Text(fontWeight = FontWeight.Bold,
							text = notNullCar.manufacturer,
							modifier = Modifier
								.sharedBounds(
									sharedContentState = rememberSharedContentState(key = "car_manufacturer_${notNullCar.id}"),
									animatedVisibilityScope = animatedVisibilityScope,
								))
					}
					Row {
						Text(text = "Model : ")
						Text(fontWeight = FontWeight.Bold,
							text = notNullCar.model,
							modifier = Modifier
								.sharedBounds(
									sharedContentState = rememberSharedContentState(key = "car_model_${notNullCar.id}"),
									animatedVisibilityScope = animatedVisibilityScope,
								))
					}
					Row {
						Text(text = "Vehicle country : ")
						Text(fontWeight = FontWeight.Bold, text = notNullCar.vehicleCountry)
					}
					Row {
						Text(text = "Filming location : ")
						Text(
							fontWeight = FontWeight.Bold,
							text = "${notNullCar.filmingLocationCity}, ${notNullCar.filmingLocationState}"
						)
					}
					//Dougscores
					Row(
						Modifier
							.fillMaxWidth()
							.padding(SPACER_WIDTH)
					) {
						Column(
							modifier = Modifier.weight(0.5f),
							horizontalAlignment = Alignment.CenterHorizontally
						) {
							Text(fontWeight = FontWeight.Bold, text = "Daily score")
						}
						Column(
							modifier = Modifier.weight(0.5f),
							horizontalAlignment = Alignment.CenterHorizontally
						) {
							Text(fontWeight = FontWeight.Bold, text = "Weekend score")
						}
					}
					DougScoreTable(notNullCar)
					Spacer(modifier = Modifier.height(DEFAULT_PADDING))
					Text(
						fontWeight = FontWeight.Bold,
						text = "Total DougScore : ${notNullCar.dougScore}",
						modifier = Modifier
							.sharedBounds(
								sharedContentState = rememberSharedContentState(key = "car_dougscore_${notNullCar.id}"),
								animatedVisibilityScope = animatedVisibilityScope,
							)
					)
					Text(
						fontWeight = FontWeight.Bold,
						text = "Global ranking : #${notNullCar.id + 1}",
						modifier = Modifier
							.sharedBounds(
								sharedContentState = rememberSharedContentState(key = "car_rank_${notNullCar.id}"),
								animatedVisibilityScope = animatedVisibilityScope,
							)
					)
					Spacer(modifier = Modifier.height(DEFAULT_PADDING))
					val videoAvailable = remember(key1 = car) {
						notNullCar.videoId != null
					}
					if (videoAvailable) {
						Button(onClick = {
							val intent = Intent(Intent.ACTION_VIEW, Uri.parse(notNullCar.videoLink))
							startActivity(context, intent, null)
						}) {
							Text(text = "Watch on youtube")
						}
					}
					Spacer(modifier = Modifier.height(DEFAULT_PADDING))
				}
			} else {
				CircularProgressIndicator()
			}
		}
	}
}