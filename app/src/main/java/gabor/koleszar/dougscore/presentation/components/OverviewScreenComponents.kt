package gabor.koleszar.dougscore.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector


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

