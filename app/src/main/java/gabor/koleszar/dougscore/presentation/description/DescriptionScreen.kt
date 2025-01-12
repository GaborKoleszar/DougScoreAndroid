package gabor.koleszar.dougscore.presentation.description

import android.graphics.fonts.FontStyle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gabor.koleszar.dougscore.R
import gabor.koleszar.dougscore.presentation.StyleConstants.DEFAULT_PADDING
import gabor.koleszar.dougscore.presentation.StyleConstants.SPACER_WIDTH

@Composable
fun DescriptionScreen(
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
			modifier = modifier
				.widthIn(300.dp, 600.dp)
				.padding(DEFAULT_PADDING)
		) {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(horizontal = DEFAULT_PADDING),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Spacer(modifier = Modifier.height(SPACER_WIDTH))
				Text(
					text = stringResource(R.string.description1),
					textAlign = TextAlign.Justify,
					letterSpacing = 0.sp
				)
				Spacer(modifier = Modifier.height(SPACER_WIDTH))
				Text(
					text = stringResource(R.string.description2),
					textAlign = TextAlign.Justify,
					letterSpacing = 0.sp
				)
				Spacer(modifier = Modifier.height(SPACER_WIDTH))
				Text(
					text = stringResource(R.string.description3),
					textAlign = TextAlign.Justify,
					letterSpacing = 0.sp
				)
				Spacer(modifier = Modifier.height(SPACER_WIDTH))
				Text(
					text = stringResource(R.string.description4),
					textAlign = TextAlign.Justify,
					letterSpacing = 0.sp
				)
				Spacer(modifier = Modifier.height(SPACER_WIDTH))
				Text(
					text = stringResource(R.string.description5),
					textAlign = TextAlign.Justify,
					letterSpacing = 0.sp
				)
				Spacer(modifier = Modifier.height(SPACER_WIDTH))
				Text(
					text = stringResource(R.string.description6),
					textAlign = TextAlign.Justify,
					letterSpacing = 0.sp
				)
				Spacer(modifier = Modifier.height(SPACER_WIDTH))
			}
		}
	}
}