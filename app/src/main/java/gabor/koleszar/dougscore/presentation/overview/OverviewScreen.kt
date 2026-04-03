@file:OptIn(ExperimentalSharedTransitionApi::class)

package gabor.koleszar.dougscore.presentation.overview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gabor.koleszar.dougscore.R
import gabor.koleszar.dougscore.presentation.StyleConstants.DEFAULT_PADDING
import gabor.koleszar.dougscore.presentation.StyleConstants.SPACER_WIDTH
import gabor.koleszar.dougscore.presentation.components.DescriptionListItem
import gabor.koleszar.dougscore.presentation.overview.components.CarListItem
import gabor.koleszar.dougscore.presentation.theme.DougScoreTheme
import kotlinx.coroutines.launch


@Composable
fun SharedTransitionScope.OverviewScreenRoot(
	overviewViewModel: OverviewViewModel,
	onCarClick: (Int) -> Unit,
	onDescriptionClick: () -> Unit,
	animatedVisibilityScope: AnimatedVisibilityScope,
) {
	val overviewState by overviewViewModel.state.collectAsStateWithLifecycle()
	OverviewScreen(
		{ action ->
			when (action) {
				is OverviewAction.CarClick -> onCarClick(action.carId)
				is OverviewAction.DescriptionClick -> onDescriptionClick()
				else -> overviewViewModel.onAction(action)
			}
		},
		overviewState,
		animatedVisibilityScope,
	)
}

@Composable
fun SharedTransitionScope.OverviewScreen(
	onAction: (OverviewAction) -> Unit,
	overviewState: OverviewState,
	animatedVisibilityScope: AnimatedVisibilityScope,
	modifier: Modifier = Modifier
) {

	val listState = rememberLazyListState()
	val showFab by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }
	val scope = rememberCoroutineScope()

	Box(
		modifier = Modifier
			.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		when {
			overviewState.isLoading && overviewState.cars.isEmpty() -> {
				Column(
					verticalArrangement = Arrangement.Center,
					horizontalAlignment = Alignment.CenterHorizontally,
					modifier = modifier.fillMaxSize()
				) {
					Text(text = stringResource(R.string.overview_scree_loading_data_please_wait))
					Spacer(modifier = Modifier.height(SPACER_WIDTH))
					CircularProgressIndicator()
				}
			}

			overviewState.cars.isNotEmpty() -> {
				LazyColumn(
					state = listState,
					modifier = modifier
						.fillMaxSize()
						.padding(horizontal = DEFAULT_PADDING)
						.testTag("car_list"),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					item {
						DescriptionListItem(
							onClick = { onAction(OverviewAction.DescriptionClick) }
						)
					}
					itemsIndexed(overviewState.cars) { _, car ->
						CarListItem(
							car,
							{ onAction(OverviewAction.CarClick(car.id)) },
							animatedVisibilityScope
						)
					}
				}
			}

			else -> {
				Text(text = stringResource(R.string.no_results_found))
			}
		}
		AnimatedVisibility(
			visible = showFab,
			enter = fadeIn(),
			exit = scaleOut() + fadeOut(),
			modifier = Modifier.align(Alignment.BottomEnd)
		) {
			FloatingActionButton(
				onClick = { scope.launch { listState.animateScrollToItem(0) } },
				modifier = Modifier.padding(24.dp),
			) {
				Icon(Icons.Default.KeyboardArrowUp, contentDescription = stringResource(R.string.scroll_to_top))
			}
		}
	}
}

/*
* * * * * * * * * *
*  PREVIEW BELOW  *
* * * * * * * * * *
*/

@Preview(
	showBackground = true, heightDp = 500, widthDp = 380
)
@Composable
fun CarListPreview() {
	DougScoreTheme {
		/*OverviewScreen(
			onCarClick = {},
			cars = dummyCars(),
			isLoading = false
		)*/
	}
}
