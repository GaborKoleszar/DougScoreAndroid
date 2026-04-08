package gabor.koleszar.dougscore.presentation.compare

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gabor.koleszar.dougscore.R
import gabor.koleszar.dougscore.domain.model.Car
import gabor.koleszar.dougscore.presentation.StyleConstants.DEFAULT_PADDING
import gabor.koleszar.dougscore.presentation.StyleConstants.SPACER_WIDTH
import gabor.koleszar.dougscore.presentation.components.AsyncImageWithMultipleFallback
import gabor.koleszar.dougscore.presentation.components.SideBySideScoreTable

@Composable
fun CompareResultScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: CompareResultViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CompareResultScreen(state = state, modifier = modifier)
}

@Composable
fun CompareResultScreen(
    state: CompareResultState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        if (state.isLoading || state.car1 == null || state.car2 == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DEFAULT_PADDING),
                horizontalArrangement = Arrangement.spacedBy(SPACER_WIDTH)
            ) {
                CarImageHeader(car = state.car1, modifier = Modifier.weight(1f))
                CarImageHeader(car = state.car2, modifier = Modifier.weight(1f))
            }
            SideBySideScoreTable(
                car1 = state.car1,
                car2 = state.car2,
                modifier = Modifier.padding(horizontal = DEFAULT_PADDING)
            )
            Spacer(modifier = Modifier.height(DEFAULT_PADDING))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DEFAULT_PADDING),
                horizontalArrangement = Arrangement.spacedBy(SPACER_WIDTH)
            ) {
                listOf(state.car1, state.car2).forEach { car ->
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        if (car.videoId != null) {
                            Button(onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(car.videoLink))
                                context.startActivity(intent)
                            }) {
                                Text(text = stringResource(R.string.watch_on_youtube))
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(DEFAULT_PADDING))
        }
    }
}

@Composable
private fun CarImageHeader(
    car: Car,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImageWithMultipleFallback(
            model = car.getMaxresImageLink(),
            fallbackModel = car.getHqFallbackImageLink(),
            modifier = Modifier.clip(RoundedCornerShape(DEFAULT_PADDING)),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.height(SPACER_WIDTH))
        Text(
            text = "${car.manufacturer} ${car.model}",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
