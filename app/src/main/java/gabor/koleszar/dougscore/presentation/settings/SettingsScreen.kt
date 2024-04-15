package gabor.koleszar.dougscore.presentation.settings

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import gabor.koleszar.dougscore.presentation.util.DateTimeFormatter
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
			.verticalScroll(rememberScrollState())
	) {
		Card(
			colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
			modifier = Modifier
				.fillMaxWidth()
				.padding(StyleConstants.DEFAULT_PADDING),
			elevation = CardDefaults.cardElevation(
				defaultElevation = StyleConstants.ELEVATION
			)
		) {
			if (isLoading) {
				LinearProgressIndicator(
					modifier = Modifier
						.fillMaxWidth()
						.height(3.dp)
				)
			} else {
				Spacer(modifier = Modifier.height(3.dp))
			}
			Column(
				modifier
					.fillMaxWidth()
					.padding(StyleConstants.DEFAULT_PADDING),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceAround,
					verticalAlignment = Alignment.CenterVertically
				) {
					Column(
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						Text(
							text = "Last refreshed"
						)
						Text(
							text = DateTimeFormatter.getTimeAgo(
								Date(lastRefreshTimeInMillis)
							),
							fontStyle = FontStyle.Italic
						)
					}
					Button(onClick = onRefreshData) {
						Text(text = "Refresh data")

					}
				}
				Spacer(modifier = Modifier.height(StyleConstants.SPACER_WIDTH))
				/*Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically
				) {
					Text(
						text = "Use device theme",
						modifier = Modifier.weight(1f),
						textAlign = TextAlign.Center
					)
					Switch(
						checked = settingsViewModel.useDeviceTheme,
						onCheckedChange = {},
						modifier = Modifier.weight(1f)
					)
				}
				Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically
				) {
					Text(
						text = "Dark theme",
						modifier = Modifier.weight(1f),
						textAlign = TextAlign.Center
					)
					Switch(
						checked = settingsViewModel.useDarkTheme,
						onCheckedChange = { },
						enabled = !settingsViewModel.useDeviceTheme,
						modifier = Modifier.weight(1f)
					)
				}*/
			}
		}
	}
}