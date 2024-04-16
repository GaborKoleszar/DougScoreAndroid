package gabor.koleszar.dougscore.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import gabor.koleszar.dougscore.presentation.StyleConstants.DEFAULT_PADDING
import gabor.koleszar.dougscore.presentation.components.BottomSheetDropdownMenu
import gabor.koleszar.dougscore.presentation.components.SearchField
import gabor.koleszar.dougscore.presentation.details.DetailsScreen
import gabor.koleszar.dougscore.presentation.overview.OverviewScreen
import gabor.koleszar.dougscore.presentation.overview.OverviewViewModel
import gabor.koleszar.dougscore.presentation.settings.SettingsScreen
import gabor.koleszar.dougscore.presentation.settings.SettingsViewModel
import gabor.koleszar.dougscore.presentation.theme.DougScoreTheme
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

	@OptIn(ExperimentalMaterial3Api::class)
	override fun onCreate(savedInstanceState: Bundle?) {
		installSplashScreen()
		enableEdgeToEdge()
		super.onCreate(savedInstanceState)

		setContent {
			val settingsViewModel = hiltViewModel<SettingsViewModel>()
			val userSettings by settingsViewModel.userSettings.collectAsStateWithLifecycle()

			/*
			 * Should use dark theme when system is dark and user wants to follow the system theme
			 * OR
			 * dark theme is enabled AND follow device theme is disabled
			 */
			val shouldUseDarkTheme =
				(isSystemInDarkTheme() && userSettings.useDeviceTheme) ||
						userSettings.useDarkTheme && !userSettings.useDeviceTheme

			DougScoreTheme(
				darkTheme = shouldUseDarkTheme,
				dynamicColor = userSettings.useDynamicColor
			) {
				val scrollBehavior =
					TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
				val navController = rememberNavController()
				val scaffoldState = rememberBottomSheetScaffoldState()
				val coroutineScope = rememberCoroutineScope()
				val currentDestination =
					navController.currentBackStackEntryAsState().value?.destination?.route
				val overviewViewModel = hiltViewModel<OverviewViewModel>()

				BottomSheetScaffold(
					scaffoldState = scaffoldState,
					topBar = {
						CenterAlignedTopAppBar(
							navigationIcon = {
								if (!currentDestination.equals(Route.OVERVIEW)) {
									IconButton(onClick = { navController.popBackStack() }) {
										Icon(
											imageVector = Icons.AutoMirrored.Filled.ArrowBack,
											contentDescription = "Open search"
										)
									}
								} else {
									IconButton(onClick = { navController.navigate(Route.SETTINGS) }) {
										Icon(
											imageVector = Icons.Default.Settings,
											contentDescription = "Settings"
										)
									}
								}
							},
							title = {
								Text(
									text = "Unofficial DougScore",
									maxLines = 1,
									overflow = TextOverflow.Ellipsis
								)
							},
							actions = {
								if (currentDestination.equals(Route.OVERVIEW)) {
									IconButton(onClick = {
										coroutineScope.launch {
											scaffoldState.bottomSheetState.expand()
										}
									}) {
										Icon(
											imageVector = Icons.Default.MoreVert,
											contentDescription = "Open filters"
										)
									}
								}
							},
							scrollBehavior = scrollBehavior,
							colors = TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
								scrolledContainerColor = TopAppBarDefaults.centerAlignedTopAppBarColors().containerColor
							)
						)
					},
					sheetPeekHeight = 0.dp,
					sheetContent = {
						Column(
							horizontalAlignment = Alignment.CenterHorizontally,
							modifier = Modifier
								.padding(DEFAULT_PADDING)
						) {
							val searchFieldValue by
								overviewViewModel.searchText.collectAsStateWithLifecycle()
							SearchField(
								searchFieldValue,
								overviewViewModel::onSearchTextChange,
								overviewViewModel::onClearSearchField
							)
							BottomSheetDropdownMenu()
						}
					},
					modifier = Modifier
						.fillMaxSize()
						.nestedScroll(scrollBehavior.nestedScrollConnection)
				) { scaffoldPadding ->
					NavHost(
						modifier = Modifier.padding(scaffoldPadding),
						navController = navController,
						startDestination = Route.OVERVIEW,
						enterTransition = {
							slideIntoContainer(
								towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
								animationSpec = tween(500)
							)
						},
						exitTransition = {
							slideOutOfContainer(
								towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
								animationSpec = tween(500)
							)
						},
						popEnterTransition = {
							slideIntoContainer(
								towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
								animationSpec = tween(500)
							)
						},
						popExitTransition = {
							slideOutOfContainer(
								towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
								animationSpec = tween(500)
							)
						}
					) {
						composable(
							route = Route.OVERVIEW
						) {
							val cars by overviewViewModel.cars.collectAsStateWithLifecycle()
							OverviewScreen(
								onCarClick = { carId ->
									navController.navigate(Route.DETAILS + "/$carId")
								},
								cars = cars,
								isLoading = overviewViewModel.isLoading
							)
						}
						composable(
							route = Route.DETAILS + "/{carId}",
							arguments = listOf(
								navArgument("carId") {
									type = NavType.IntType
								}
							)
						) {
							val carId = it.arguments?.getInt("carId")!!
							DetailsScreen(carId = carId)
						}
						composable(
							route = Route.SETTINGS
						) {
							val lastUpdated by settingsViewModel.lastUpdatedTimeStamp.collectAsStateWithLifecycle()
							SettingsScreen(
								lastRefreshTimeInMillis = lastUpdated,
								isLoading = overviewViewModel.isLoading,
								onRefreshData = overviewViewModel::refresh,
								userSettings = userSettings,
								handleEvent = settingsViewModel::handleEvent
							)
						}
					}
				}
			}
		}
	}
}