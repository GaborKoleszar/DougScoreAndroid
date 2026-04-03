package gabor.koleszar.dougscore.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import gabor.koleszar.dougscore.R
import gabor.koleszar.dougscore.presentation.Route
import gabor.koleszar.dougscore.presentation.StyleConstants
import gabor.koleszar.dougscore.presentation.StyleConstants.DEFAULT_PADDING
import gabor.koleszar.dougscore.presentation.StyleConstants.SPACER_WIDTH


@Composable
fun AnimatedBackButton(
	currentDestination: String?,
	navController: NavController,
) {
	var icon = Icons.Default.Settings
	var contentDescription = stringResource(R.string.settings_content_description)
	var onClick = { navController.navigate(Route.Settings) }

	currentDestination?.let {
		if (!it.endsWith(Route.OverView.javaClass.simpleName)) {
			icon = Icons.AutoMirrored.Default.ArrowBack
			contentDescription = stringResource(R.string.back_content_description)
			onClick = { navController.navigateUp() }
		}
	}

	AnimatedContent(targetState = icon) { targetIcon ->
		IconButton(onClick) {
			Icon(
				imageVector = targetIcon,
				contentDescription = contentDescription,
			)
		}
	}
}

@Composable
fun DescriptionListItem(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Card(
		colors = CardDefaults.cardColors(
			containerColor = CardDefaults.cardColors().containerColor.copy(alpha = 0.2f)
		),
		modifier = modifier
            .widthIn(300.dp, 600.dp)
			.clip(RoundedCornerShape(DEFAULT_PADDING))
            .clickable(onClick = onClick),
		elevation = CardDefaults.cardElevation(StyleConstants.ZERO_ELEVATION)
	) {
		Box(
			modifier = Modifier
                .fillMaxWidth()
                .padding(DEFAULT_PADDING),
			contentAlignment = Alignment.Center
		) {
			Text(
				fontWeight = FontWeight.SemiBold,
				text = "How does the DougScore work?"
			)
		}
	}
	Spacer(
		modifier = Modifier
			.height(SPACER_WIDTH)
	)
}

