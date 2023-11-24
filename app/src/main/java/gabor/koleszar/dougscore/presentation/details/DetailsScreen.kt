package gabor.koleszar.dougscore.presentation.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import coil.compose.AsyncImage
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
			model = car?.imageLink,
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
				Text(text = car?.manufacturer ?: "")
				Text(text = car?.model ?: "")
				Text(text = car?.vehicleCountry ?: "")
				Text(text = ("Filmed at: " + car?.filmingLocationCity))
			}
		}
	}
}