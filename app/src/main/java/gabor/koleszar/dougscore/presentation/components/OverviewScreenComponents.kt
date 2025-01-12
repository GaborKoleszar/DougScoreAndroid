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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import gabor.koleszar.dougscore.presentation.StyleConstants
import gabor.koleszar.dougscore.presentation.StyleConstants.DEFAULT_PADDING
import gabor.koleszar.dougscore.presentation.StyleConstants.SPACER_WIDTH


@Composable
fun AnimatedIcon(
	icon1: ImageVector,
	contentDescription1: String = "",
	onClick1: () -> Unit,
	icon2: ImageVector,
	contentDescription2: String = "",
	onClick2: () -> Unit,
	showButton1: Boolean = true
) {
	val icon = remember(showButton1) {
		if (showButton1) icon1 else icon2
	}
	val onClick = remember(icon) {
		if (showButton1) onClick1 else onClick2
	}
	val contentDescription = remember(icon) {
		if (showButton1) contentDescription1 else contentDescription2
	}

	AnimatedContent(targetState = icon, label = "") { targetIcon ->
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
	modifier: Modifier = Modifier.widthIn(300.dp, 600.dp),
	onClick: () -> Unit,
) {
	Card(
		shape = RoundedCornerShape(DEFAULT_PADDING),
		colors = CardDefaults.cardColors(
			containerColor = CardDefaults.cardColors().containerColor.copy(alpha = 0.2f)
		),
		modifier = modifier
			//.fillMaxWidth()
			.clickable(onClick = onClick),
		elevation = CardDefaults.cardElevation(StyleConstants.ZERO_ELEVATION)
	) {
		Box(
			modifier = modifier
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
		modifier = modifier
			.height(SPACER_WIDTH)
	)
}

