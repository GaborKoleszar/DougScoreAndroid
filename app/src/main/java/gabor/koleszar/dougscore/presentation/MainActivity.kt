package gabor.koleszar.dougscore.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.style.TextOverflow
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
import gabor.koleszar.dougscore.presentation.components.AnimatedIcon
import gabor.koleszar.dougscore.presentation.components.BottomSheetContent
import gabor.koleszar.dougscore.presentation.description.DescriptionScreen
import gabor.koleszar.dougscore.presentation.details.DetailsScreen
import gabor.koleszar.dougscore.presentation.overview.OverviewScreen
import gabor.koleszar.dougscore.presentation.overview.OverviewViewModel
import gabor.koleszar.dougscore.presentation.settings.SettingsScreen
import gabor.koleszar.dougscore.presentation.settings.SettingsViewModel
import gabor.koleszar.dougscore.presentation.theme.DougScoreTheme

@OptIn(ExperimentalComposeUiApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

	@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
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
				val sheetState = rememberModalBottomSheetState()
				var isSheetOpen by rememberSaveable { mutableStateOf(false) }
				val currentDestination =
					navController.currentBackStackEntryAsState().value?.destination?.route
				val overviewViewModel = hiltViewModel<OverviewViewModel>()

				Scaffold(
					topBar = {
						CenterAlignedTopAppBar(
							navigationIcon = {
								AnimatedIcon(
									icon1 = Icons.Default.Settings,
									contentDescription1 = "Settings",
									onClick1 = { navController.navigate(Route.SETTINGS) },
									icon2 = Icons.AutoMirrored.Default.ArrowBack,
									contentDescription2 = "Back",
									onClick2 = { navController.popBackStack() },
									showButton1 = currentDestination.equals(Route.OVERVIEW)
								)
							},
							title = {
								Text(
									text = "DougScore",
									maxLines = 1,
									overflow = TextOverflow.Ellipsis
								)
							},
							actions = {
								if (currentDestination.equals(Route.OVERVIEW)) {
									IconButton(onClick = {
										isSheetOpen = true
									}) {
										Icon(
											imageVector = Icons.Default.Search,
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
					modifier = Modifier
						.fillMaxSize()
						.nestedScroll(scrollBehavior.nestedScrollConnection)
				) { scaffoldPadding ->
					SharedTransitionLayout {
						NavHost(
							modifier = Modifier
								.padding(scaffoldPadding)
								.semantics { testTagsAsResourceId = true },
							navController = navController,
							startDestination = Route.OVERVIEW,
						) {
							composable(
								route = Route.OVERVIEW
							) {
								val cars by overviewViewModel.cars.collectAsStateWithLifecycle()
								OverviewScreen(
									onCarClick = { carId ->
										navController.navigate(Route.DETAILS + "/$carId")
									},
									onDescriptionClick = {
										navController.navigate(Route.DESCRIPTION)
									},
									cars = cars,
									isLoading = overviewViewModel.isLoading.collectAsStateWithLifecycle().value,
									animatedVisibilityScope = this@composable,
								)
								if (isSheetOpen) {
									ModalBottomSheet(
										sheetState = sheetState,
										onDismissRequest = { isSheetOpen = false }
									) {
										BottomSheetContent(
											overviewViewModel.searchText.collectAsStateWithLifecycle().value,
											overviewViewModel.isDescending.collectAsStateWithLifecycle().value,
											overviewViewModel::onSearchTextChange,
											overviewViewModel::onClearSearchField,
											overviewViewModel::handleEvent
										)
									}
								}
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
								DetailsScreen(
									carId = carId,
									animatedVisibilityScope = this@composable,
								)
							}
							composable(
								route = Route.SETTINGS
							) {
								val lastUpdated by settingsViewModel.lastUpdatedTimeStamp.collectAsStateWithLifecycle()
								SettingsScreen(
									lastRefreshTimeInMillis = lastUpdated,
									isLoading = overviewViewModel.isLoading.collectAsStateWithLifecycle().value,
									onRefreshData = overviewViewModel::refresh,
									userSettings = userSettings,
									handleEvent = settingsViewModel::handleEvent,
								)
							}
							composable(
								route = Route.DESCRIPTION
							) {
								DescriptionScreen(
								)
							}
						}
					}
				}
			}
		}
	}
}