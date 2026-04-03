package gabor.koleszar.dougscore.presentation.settings

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import gabor.koleszar.dougscore.R
import gabor.koleszar.dougscore.domain.model.UserSettings
import gabor.koleszar.dougscore.presentation.StyleConstants
import gabor.koleszar.dougscore.presentation.util.DateTimeFormatter
import java.util.Date

@Composable
fun SettingsScreen(
	lastRefreshTimeInMillis: Long,
	isLoading: Boolean,
	settingsState: SettingsState,
	onAction: (SettingsAction) -> Unit,
	modifier: Modifier = Modifier
) {
	Box(
		modifier = modifier
			.fillMaxSize()
			.verticalScroll(rememberScrollState()),
		contentAlignment = Alignment.TopCenter
	) {
		Card(
			colors = CardDefaults.cardColors(
				containerColor = CardDefaults.cardColors().containerColor.copy(alpha = 0.2f)
			),
			modifier = Modifier
				.widthIn(300.dp, 600.dp)
				.padding(StyleConstants.DEFAULT_PADDING)
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
							text = stringResource(R.string.last_refreshed)
						)
						Text(
							text = DateTimeFormatter.getTimeAgo(
								Date(lastRefreshTimeInMillis)
							),
							fontStyle = FontStyle.Italic
						)
					}
					Button(
						onClick = { onAction(SettingsAction.RefreshCars) },
						enabled = !isLoading
					) {
						Text(text = stringResource(R.string.refresh_data))

					}
				}
				Spacer(modifier = Modifier.height(StyleConstants.SPACER_WIDTH))
				Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically
				) {
					Text(
						text = stringResource(R.string.use_device_theme),
						modifier = Modifier.weight(1f),
						textAlign = TextAlign.Center
					)
					Switch(
						checked = settingsState.useDeviceTheme,
						onCheckedChange = { onAction(SettingsAction.ToggleDeviceTheme) },
						modifier = Modifier.weight(1f)
					)
				}
				Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically
				) {
					Text(
						text = stringResource(R.string.dark_theme),
						modifier = Modifier.weight(1f),
						textAlign = TextAlign.Center
					)
					Switch(
						checked = settingsState.useDarkTheme,
						onCheckedChange = { onAction(SettingsAction.ToggleDarkTheme) },
						enabled = !settingsState.useDeviceTheme,
						modifier = Modifier.weight(1f)
					)
				}
				Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically
				) {
					Text(
						text = stringResource(R.string.dynamic_colors),
						modifier = Modifier.weight(1f),
						textAlign = TextAlign.Center
					)
					Switch(
						checked = settingsState.useDynamicColor,
						onCheckedChange = { onAction(SettingsAction.ToggleDynamicColor) },
						enabled = supportsDynamicTheming(),
						modifier = Modifier.weight(1f)
					)
				}
				Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically
				) {
					val urlContext = LocalContext.current
					Text(
						color = Color.Gray,
						text = stringResource(R.string.doug_demuro_website),
						modifier = Modifier
							.weight(1f)
							.clickable {
								urlContext.startActivity(
									Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dougdemuro.com"))
								)
							},
						textAlign = TextAlign.Center
					)
				}
				Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically
				) {
					val context = LocalContext.current
					val version = remember {
						context.packageManager.getPackageInfo(
							context.packageName,
							0
						).versionName
					}
					Text(
						color = Color.Gray,
						text = stringResource(R.string.version_label, version ?: ""),
						modifier = Modifier.weight(1f),
						textAlign = TextAlign.Center
					)
				}
			}
		}
	}
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S