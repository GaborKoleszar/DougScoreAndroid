package gabor.koleszar.dougscore.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import gabor.koleszar.dougscore.presentation.StyleConstants
import gabor.koleszar.dougscore.presentation.StyleConstants.DEFAULT_PADDING
import gabor.koleszar.dougscore.presentation.overview.OverviewEvent

@Composable
fun SearchField(
	searchValue: String,
	onSearchTextChange: (String) -> Unit,
	onClearSearchField: () -> Unit
) {
	TextField(
		value = searchValue,
		onValueChange = onSearchTextChange,
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
				modifier = Modifier.clickable(onClick = onClearSearchField)
			)
		},
		placeholder = { Text("Filter car by name") },
		textStyle = MaterialTheme.typography.bodyLarge,
		shape = RoundedCornerShape(StyleConstants.BORDER_RADIUS),
		maxLines = 1,
		singleLine = true,
		modifier = Modifier
			.fillMaxWidth()
			.padding(DEFAULT_PADDING),
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
	onSearchTextChange: (String) -> Unit,
	onClearSearchField: () -> Unit,
	onCheckedChange: (OverviewEvent) -> Unit
) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier
			.padding(DEFAULT_PADDING)
	) {
		SearchField(
			searchText,
			onSearchTextChange,
			onClearSearchField
		)
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = "Reverse ordering",
				modifier = Modifier.weight(1f),
				textAlign = TextAlign.Center
			)
			Switch(
				checked = isDescendingOrder,
				onCheckedChange = { onCheckedChange(OverviewEvent.TOGGLE_IS_DESCENDING) },
				modifier = Modifier.weight(1f)
			)
		}
	}
}