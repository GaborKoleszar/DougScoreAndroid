@file:OptIn(ExperimentalSharedTransitionApi::class)

package gabor.koleszar.dougscore.presentation.overview

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gabor.koleszar.dougscore.R
import gabor.koleszar.dougscore.presentation.StyleConstants.DEFAULT_PADDING
import gabor.koleszar.dougscore.presentation.StyleConstants.SPACER_WIDTH
import gabor.koleszar.dougscore.presentation.components.DescriptionListItem
import gabor.koleszar.dougscore.presentation.overview.components.CarListItem
import gabor.koleszar.dougscore.presentation.theme.DougScoreTheme


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

	Box(
		modifier = Modifier
			.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		if (overviewState.cars.isNotEmpty()) {
			LazyColumn(
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
				if (overviewState.isLoading) {
					item {
						Column(
							verticalArrangement = Arrangement.Center,
							horizontalAlignment = Alignment.CenterHorizontally,
							modifier = modifier
								.fillMaxSize()
						) {
							Text(text = stringResource(R.string.overview_scree_loading_data_please_wait))
							Spacer(modifier = Modifier.height(SPACER_WIDTH))
							CircularProgressIndicator()
						}
					}
				} else {
					itemsIndexed(overviewState.cars) { _, car ->
						CarListItem(
							car,
							{ onAction(OverviewAction.CarClick(car.id)) },
							animatedVisibilityScope
						)
					}
				}
			}
		} else {
			Text(text = "No results found.")
		}
	}
}

@Composable
fun InitialListView(modifier: Modifier = Modifier) {
	Column(
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier
			.fillMaxSize()
	) {
		Text(text = stringResource(R.string.overview_scree_loading_data_please_wait))
		Spacer(modifier = Modifier.height(SPACER_WIDTH))
		CircularProgressIndicator()
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
