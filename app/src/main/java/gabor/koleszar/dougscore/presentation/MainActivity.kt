package gabor.koleszar.dougscore.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import gabor.koleszar.dougscore.presentation.StyleConstants.BORDER_RADIUS
import gabor.koleszar.dougscore.presentation.StyleConstants.DEFAULT_PADDING
import gabor.koleszar.dougscore.presentation.StyleConstants.SPACER_WIDTH
import gabor.koleszar.dougscore.presentation.details.DetailsScreen
import gabor.koleszar.dougscore.presentation.overview.MainViewModel
import gabor.koleszar.dougscore.presentation.overview.OverviewScreen
import gabor.koleszar.dougscore.presentation.theme.DougScoreTheme
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	@OptIn(ExperimentalMaterial3Api::class)
	override fun onCreate(savedInstanceState: Bundle?) {
		installSplashScreen()
		enableEdgeToEdge()
		super.onCreate(savedInstanceState)

		val mainViewModel by viewModels<MainViewModel>()
		setContent {
			DougScoreTheme {
				val scrollBehavior =
					TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
				val navController = rememberNavController()
				val scaffoldState = rememberBottomSheetScaffoldState()
				val coroutineScope = rememberCoroutineScope()

				BottomSheetScaffold(
					scaffoldState = scaffoldState,
					topBar = {
						CenterAlignedTopAppBar(
							navigationIcon = {

							},
							title = {
								Text(
									text = "Unofficial DougScore",
									maxLines = 1,
									overflow = TextOverflow.Ellipsis
								)
							},
							actions = {
								IconButton(onClick = {
									coroutineScope.launch {
										scaffoldState.bottomSheetState.expand()
									}
								}) {
									Icon(
										imageVector = Icons.Default.Search,
										contentDescription = "Open search"
									)
								}
							},
							scrollBehavior = scrollBehavior
						)
					},
					sheetShape = RoundedCornerShape(
						topStart = BORDER_RADIUS,
						topEnd = BORDER_RADIUS
					),
					sheetPeekHeight = 0.dp,
					sheetContent = {
						Column(
							horizontalAlignment = Alignment.CenterHorizontally,
							modifier = Modifier
								.padding(DEFAULT_PADDING)
						) {
							Text(text = "Filters", style = MaterialTheme.typography.labelLarge)
							Spacer(modifier = Modifier.height(SPACER_WIDTH))
							TextField(
								value = mainViewModel.searchText.collectAsState().value,
								onValueChange = mainViewModel::onSearchTextChange,
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
										modifier = Modifier.clickable {
											mainViewModel.clearSearchField()
										}
									)
								},
								placeholder = { Text("Search...") },
								textStyle = MaterialTheme.typography.bodyLarge,
								shape = RoundedCornerShape(BORDER_RADIUS),
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
						}
					},
					modifier = Modifier
						.fillMaxSize()
						.nestedScroll(scrollBehavior.nestedScrollConnection)
				) { scaffoldPadding ->
					NavHost(
						modifier = Modifier.padding(scaffoldPadding),
						navController = navController,
						startDestination = Route.OVERVIEW
					) {
						composable(
							route = Route.OVERVIEW
						) {
							val cars by mainViewModel.cars.collectAsState()
							OverviewScreen(
								onCarClick = { carId ->
									mainViewModel.onCarSelected(carId)
									navController.navigate(Route.DETAILS)
								},
								cars = cars
							)
						}
						composable(route = Route.DETAILS) {
							val car by mainViewModel.carInDetailsScreen.collectAsState()
							car?.let { nonNullCar ->
								DetailsScreen(
									car = nonNullCar
								)
							}
						}
					}
				}
			}
		}
	}
}