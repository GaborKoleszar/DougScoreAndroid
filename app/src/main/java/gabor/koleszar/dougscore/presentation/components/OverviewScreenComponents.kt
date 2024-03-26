package gabor.koleszar.dougscore.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import gabor.koleszar.dougscore.R

@Composable
fun AsyncImageWithFallbackUrl(
	model: Any?,
	fallbackModel: Any?,
	modifier: Modifier = Modifier
) {
	var fallbackNeeded by rememberSaveable {
		mutableStateOf(false)
	}
	if (fallbackNeeded) {
		AsyncImage(
			model = fallbackModel,
			contentDescription = null,
			error = painterResource(id = R.drawable.placeholder),
			modifier = modifier,
			contentScale = ContentScale.Crop
		)
	} else {
		AsyncImage(
			model = model,
			contentDescription = null,
			onError = { fallbackNeeded = true },
			modifier = modifier,
			contentScale = ContentScale.Crop
		)
	}
}