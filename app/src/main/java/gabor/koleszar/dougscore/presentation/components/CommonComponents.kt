package gabor.koleszar.dougscore.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import gabor.koleszar.dougscore.R

@Composable
fun AsyncImageWithMultipleFallback(
	model: Any?,
	fallbackModel: Any?,
	modifier: Modifier = Modifier,
	contentScale: ContentScale? = null,
	filterQuality: FilterQuality? = null
) {
	var fallbackNeeded by rememberSaveable {
		mutableStateOf(false)
	}
	if (fallbackNeeded) {
		AsyncImage(
			model = fallbackModel,
			contentDescription = "car image",
			error = painterResource(id = R.drawable.placeholder),
			modifier = modifier,
			contentScale = contentScale ?: ContentScale.Crop,
			filterQuality = filterQuality ?: FilterQuality.Low
		)
	} else {
		AsyncImage(
			model = model,
			contentDescription = "car image",
			onError = { fallbackNeeded = true },
			modifier = modifier,
			contentScale = contentScale ?: ContentScale.Crop,
			filterQuality = filterQuality ?: FilterQuality.Low
		)
	}
}