package gabor.koleszar.dougscore.presentation.details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import gabor.koleszar.dougscore.R
import gabor.koleszar.dougscore.domain.model.Car
import gabor.koleszar.dougscore.presentation.StyleConstants
import gabor.koleszar.dougscore.presentation.StyleConstants.DEFAULT_PADDING
import gabor.koleszar.dougscore.presentation.StyleConstants.SPACER_WIDTH
import gabor.koleszar.dougscore.presentation.components.DougScoreTable

@Composable
fun DetailsScreen(
	modifier: Modifier = Modifier,
	car: Car
) {
	Box(
		modifier = modifier
			.fillMaxWidth()
			.verticalScroll(rememberScrollState())
	) {
		val context = LocalContext.current
		Card(
			colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
			modifier = Modifier
				.fillMaxWidth()
				.padding(DEFAULT_PADDING),
			elevation = CardDefaults.cardElevation(
				defaultElevation = StyleConstants.ELEVATION
			)
		) {
			Column(
				modifier.fillMaxWidth(),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				AsyncImage(
					model = car.getMaxresImageLink(),
					contentDescription = null,
					error = painterResource(id = R.drawable.placeholder),
					placeholder = painterResource(id = R.drawable.placeholder),
					modifier = Modifier.fillMaxWidth(),
					contentScale = ContentScale.FillWidth
				)
				//Basic info
				Spacer(modifier = Modifier.height(DEFAULT_PADDING))
				Row {
					Text(text = "Manufacturer : ")
					Text(fontWeight = FontWeight.Bold, text = car.manufacturer)
				}
				Row {
					Text(text = "Model : ")
					Text(fontWeight = FontWeight.Bold, text = car.model)
				}
				Row {
					Text(text = "Vehicle country : ")
					Text(fontWeight = FontWeight.Bold, text = car.vehicleCountry)
				}
				Row {
					Text(text = "Filming location : ")
					Text(
						fontWeight = FontWeight.Bold,
						text = "${car.filmingLocationCity}, ${car.filmingLocationState}"
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
				DougScoreTable(car)
				Spacer(modifier = Modifier.height(DEFAULT_PADDING))
				Text(fontWeight = FontWeight.Bold, text = "Total DougScore : ${car.dougScore}")
				Text(fontWeight = FontWeight.Bold, text = "Global ranking : #${car.id + 1}")
				Spacer(modifier = Modifier.height(DEFAULT_PADDING))
				val videoAvailable = remember(key1 = car) {
					car.videoId != null
				}
				if (videoAvailable) {
					Button(onClick = {
						val intent = Intent(Intent.ACTION_VIEW, Uri.parse(car.videoLink))
						startActivity(context, intent, null)
					}) {
						Text(text = "Watch on youtube")
					}
				}
				Spacer(modifier = Modifier.height(DEFAULT_PADDING))
			}
		}
	}
}