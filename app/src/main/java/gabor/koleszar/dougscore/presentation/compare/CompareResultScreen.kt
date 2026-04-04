package gabor.koleszar.dougscore.presentation.compare

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gabor.koleszar.dougscore.R
import gabor.koleszar.dougscore.domain.model.Car
import gabor.koleszar.dougscore.presentation.StyleConstants.DEFAULT_PADDING
import gabor.koleszar.dougscore.presentation.StyleConstants.SPACER_WIDTH
import gabor.koleszar.dougscore.presentation.components.AsyncImageWithMultipleFallback
import gabor.koleszar.dougscore.presentation.components.CompareScoreTable

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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        if (state.isLoading || state.car1 == null || state.car2 == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            CarComparePanel(car = state.car1, otherCar = state.car2)
            HorizontalDivider(modifier = Modifier.padding(vertical = DEFAULT_PADDING))
            CarComparePanel(car = state.car2, otherCar = state.car1)
        }
    }
}

@Composable
private fun CarComparePanel(
    car: Car,
    otherCar: Car,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(DEFAULT_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImageWithMultipleFallback(
            model = car.getMaxresImageLink(),
            fallbackModel = car.getHqFallbackImageLink(),
            modifier = Modifier
                .widthIn(300.dp, 600.dp)
                .clip(RoundedCornerShape(DEFAULT_PADDING)),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.height(DEFAULT_PADDING))
        Row {
            Text(text = car.manufacturer, fontWeight = FontWeight.Bold)
            Text(text = " ${car.model}", fontWeight = FontWeight.Bold)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SPACER_WIDTH)
        ) {
            Column(
                modifier = Modifier.weight(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(fontWeight = FontWeight.Bold, text = stringResource(R.string.daily_score))
            }
            Column(
                modifier = Modifier.weight(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(fontWeight = FontWeight.Bold, text = stringResource(R.string.weekend_score))
            }
        }
        CompareScoreTable(thisCar = car, otherCar = otherCar)
    }
}
