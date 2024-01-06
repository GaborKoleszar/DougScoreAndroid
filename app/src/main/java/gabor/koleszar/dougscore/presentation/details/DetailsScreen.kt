package gabor.koleszar.dougscore.presentation.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import gabor.koleszar.dougscore.common.Constants.YT_IMAGE_MAXRESDEFAULT
import gabor.koleszar.dougscore.presentation.StyleConstants
import gabor.koleszar.dougscore.presentation.StyleConstants.DEFAULT_PADDING
import gabor.koleszar.dougscore.presentation.StyleConstants.SPACER_WIDTH

@Composable
fun DetailsScreen(
	modifier: Modifier = Modifier,
	detailsState: DetailsState
) {
	val car = remember {
		detailsState.car
	}
	Column(
		modifier = modifier.fillMaxSize()
	) {
		Card(
			colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
			modifier = Modifier
				.fillMaxWidth()
				.padding(StyleConstants.DEFAULT_PADDING),
			elevation = CardDefaults.cardElevation(
				defaultElevation = 5.dp
			)
		) {
			AsyncImage(
				model = car?.imageLink + YT_IMAGE_MAXRESDEFAULT,
				contentDescription = null
			)
			//Basic info
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(DEFAULT_PADDING)
			) {
				Column(
					modifier = Modifier.weight(0.5f),
					horizontalAlignment = Alignment.End
				) {
					Text(text = "Manufacturer  -")
					Text(text = "Model  -")
					Text(text = "Vehicle country  -")
					Text(text = "Filming location  -")
				}
				Spacer(modifier = Modifier.width(SPACER_WIDTH))
				Column(
					modifier = Modifier.weight(0.5f)
				) {
					Text(text = car?.manufacturer ?: "")
					Text(text = car?.model ?: "")
					Text(text = car?.vehicleCountry ?: "")
					Text(text = car?.filmingLocationState + ", " + car?.filmingLocationCity)
				}
			}
			//Dougscores
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(DEFAULT_PADDING)
			) {
				Column(
					modifier = Modifier.weight(0.5f),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Text(text = "Daily score")
				}
				Column(
					modifier = Modifier.weight(0.5f),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Text(text = "Weekend score")
				}
			}
		}
	}
}