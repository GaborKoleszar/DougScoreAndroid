package gabor.koleszar.dougscore.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import gabor.koleszar.dougscore.presentation.StyleConstants
import gabor.koleszar.dougscore.presentation.StyleConstants.DEFAULT_PADDING
import gabor.koleszar.dougscore.presentation.overview.OverviewAction

@Composable
fun SearchField(
    searchValue: String,
    onAction: (OverviewAction) -> Unit
) {
    TextField(
        value = searchValue,
        onValueChange = { onAction(OverviewAction.SearchTextChange(it)) },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search icon",
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
        trailingIcon = {
            Icon(
                Icons.Default.Clear,
                contentDescription = "Clear search",
                modifier = Modifier.clickable(onClick = {
                    onAction(OverviewAction.ClearSearchField)
                })
            )
        },
        placeholder = { Text("Filter car by name") },
        textStyle = MaterialTheme.typography.bodyLarge,
        shape = RoundedCornerShape(StyleConstants.BORDER_RADIUS),
        maxLines = 1,
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        )
    )
}

@Composable
fun BottomSheetContent(
    searchText: String,
    isDescendingOrder: Boolean,
    availableManufacturers: List<String>,
    selectedManufacturers: Set<String>,
    availableCountries: List<String>,
    selectedCountries: Set<String>,
    onAction: (OverviewAction) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(DEFAULT_PADDING)
            .verticalScroll(rememberScrollState())
    ) {
        SearchField(searchText, onAction)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Reverse ordering")
            Switch(
                checked = isDescendingOrder,
                onCheckedChange = { onAction(OverviewAction.ToggleIsDescending) },
            )
        }
        FilterChipSection(
            label = "Manufacturer",
            items = availableManufacturers,
            selectedItems = selectedManufacturers,
            onToggle = { onAction(OverviewAction.ToggleManufacturerFilter(it)) },
            onClear = { onAction(OverviewAction.ClearManufacturerFilters) },
        )
        FilterChipSection(
            label = "Country",
            items = availableCountries,
            selectedItems = selectedCountries,
            onToggle = { onAction(OverviewAction.ToggleCountryFilter(it)) },
            onClear = { onAction(OverviewAction.ClearCountryFilters) },
        )
    }
}

@Composable
private fun FilterChipSection(
    label: String,
    items: List<String>,
    selectedItems: Set<String>,
    onToggle: (String) -> Unit,
    onClear: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label)
            if (selectedItems.isNotEmpty()) {
                TextButton(onClick = onClear) {
                    Text("Clear")
                }
            }
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(items) { item ->
                FilterChip(
                    selected = item in selectedItems,
                    onClick = { onToggle(item) },
                    label = {
                        Text(
                            text = item,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    modifier = Modifier.widthIn(max = 120.dp),
                )
            }
        }
    }
}
