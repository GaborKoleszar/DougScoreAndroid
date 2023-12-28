package gabor.koleszar.dougscore.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import coil.compose.AsyncImage
import gabor.koleszar.dougscore.common.Constants.YT_IMAGE_MAXRESDEFAULT
import gabor.koleszar.dougscore.presentation.StyleConstants

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
		AsyncImage(
			model = car?.imageLink + YT_IMAGE_MAXRESDEFAULT,
			contentDescription = null,
			filterQuality = FilterQuality.High
		)
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.padding(StyleConstants.DEFAULT_PADDING)
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(StyleConstants.DEFAULT_PADDING)
			) {
				Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
					Text(text = "Manufacturer:")
					Text(text = car?.manufacturer ?: "")
				}
				Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
					Text(text = "Model:")
					Text(text = car?.model ?: "")
				}
				Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
					Text(text = "Model:")
					Text(text = car?.model ?: "")
				}
				Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
					Text(text = "Vehicle country:")
					Text(text = car?.vehicleCountry ?: "")
				}
				Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
					Text(text = "Filming location:")
					Text(text = car?.filmingLocationState + ", " + car?.filmingLocationCity)
				}
			}
		}
	}
}