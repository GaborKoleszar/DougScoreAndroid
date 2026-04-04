package gabor.koleszar.dougscore.presentation.compare

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@Composable
fun CarPickerScreenRoot(
    onCarClick: (carId: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CarPickerViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CarPickerScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is CarPickerAction.CarClick -> onCarClick(action.carId)
                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier,
    )
}

@Composable
fun CarPickerScreen(
    state: CarPickerState,
    onAction: (CarPickerAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { onAction(CarPickerAction.SearchTextChange(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DEFAULT_PADDING, vertical = SPACER_WIDTH),
            placeholder = { Text(stringResource(R.string.car_picker_search_hint)) },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        )

        when {
            state.isLoading && state.cars.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            state.cars.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(stringResource(R.string.no_results_found))
                }
            }

            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.cars) { car ->
                        CompactCarListItem(
                            car = car,
                            onCarClick = { onAction(CarPickerAction.CarClick(car.id)) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CompactCarListItem(
    car: Car,
    onCarClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(SPACER_WIDTH)
    )
    Row(
        modifier = modifier
            .height(70.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(DEFAULT_PADDING))
            .clickable(onClick = onCarClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImageWithMultipleFallback(
            model = car.getMaxresImageLink(),
            fallbackModel = car.getHqFallbackImageLink(),
            modifier = Modifier
                .width(120.dp)
                .clip(RoundedCornerShape(DEFAULT_PADDING)),
        )
        Column(
            modifier = Modifier
                .padding(horizontal = DEFAULT_PADDING)
                .weight(1f),
        ) {
            Text(text = car.manufacturer, fontWeight = FontWeight.Bold)
            Text(text = car.model)
            Text(text = "Score: ${car.dougScore}")
        }
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(SPACER_WIDTH)
    )
}
