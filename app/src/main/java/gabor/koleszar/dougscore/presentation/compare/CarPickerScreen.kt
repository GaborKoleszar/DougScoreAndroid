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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarPickerScreen(
    state: CarPickerState,
    onAction: (CarPickerAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = state.searchQuery,
                    onQueryChange = { onAction(CarPickerAction.SearchTextChange(it)) },
                    onSearch = {},
                    expanded = false,
                    onExpandedChange = {},
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                        )
                    },
                    placeholder = { Text("Search cars...") },
                )
            },
            expanded = false,
            onExpandedChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DEFAULT_PADDING),
        ) {}

        when {
            state.isLoading -> {
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
                    Text("No results found")
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
fun CompactCarListItem(
    car: Car,
    onCarClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(SPACER_WIDTH)
    )
    Box(
        modifier = modifier
            .height(70.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(DEFAULT_PADDING))
            .clickable(onClick = onCarClick),
    ) {
        Row(
            modifier = Modifier
                .height(70.dp)
                .fillMaxWidth(),
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
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(SPACER_WIDTH)
    )
}
