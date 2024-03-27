package gabor.koleszar.dougscore.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gabor.koleszar.dougscore.common.DateTimeFormatter
import gabor.koleszar.dougscore.presentation.StyleConstants
import java.util.Date

@Composable
fun SettingsScreen(
	lastRefreshTimeInMillis: Long,
	isLoading: Boolean,
	onRefreshData: () -> Unit,
	modifier: Modifier = Modifier
) {
	Box(
		modifier = modifier
			.fillMaxWidth()
			.padding(StyleConstants.DEFAULT_PADDING)
			.verticalScroll(rememberScrollState())
	) {
		Card(
			colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
			modifier = Modifier
				.fillMaxWidth(),
			elevation = CardDefaults.cardElevation(
				defaultElevation = 5.dp
			)
		) {
			Column(
				modifier.fillMaxWidth(),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Row(
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Button(onClick = onRefreshData) {
						Text(text = "Refresh data")
					}
					Text(
						text = "Last refresh:\n${
							DateTimeFormatter.getDefaultFormattedDate(
								Date(lastRefreshTimeInMillis)
							)
						}"
					)
				}
				if (isLoading) {
					CircularProgressIndicator()
				}
			}
		}
	}
}