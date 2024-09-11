package gabor.koleszar.dougscore.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import gabor.koleszar.dougscore.presentation.StyleConstants

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
			.fillMaxWidth(),
		colors = TextFieldDefaults.colors(
			focusedIndicatorColor = Color.Transparent,
			unfocusedIndicatorColor = Color.Transparent,
			disabledIndicatorColor = Color.Transparent,
		)
	)
	Spacer(modifier = Modifier.height(StyleConstants.DEFAULT_PADDING))
}

@Composable
fun BottomSheetDropdownMenu() {
	var isMenuVisible by rememberSaveable {
		mutableStateOf(false)
	}
	DropdownMenu(
		expanded = isMenuVisible,
		onDismissRequest = { isMenuVisible = false })
	{
		Column {
			Text(text = "Item1")
			Text(text = "Item2")
			Text(text = "Item3")
		}
	}
}