package gabor.koleszar.dougscore.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import gabor.koleszar.dougscore.R
import gabor.koleszar.dougscore.presentation.components.AnimatedBackButton
import gabor.koleszar.dougscore.presentation.components.BottomSheetContent
import gabor.koleszar.dougscore.presentation.description.DescriptionScreen
import gabor.koleszar.dougscore.presentation.details.DetailsScreen
import gabor.koleszar.dougscore.presentation.overview.OverviewScreenRoot
import gabor.koleszar.dougscore.presentation.overview.OverviewViewModel
import gabor.koleszar.dougscore.presentation.settings.SettingsScreen
import gabor.koleszar.dougscore.presentation.settings.SettingsViewModel
import gabor.koleszar.dougscore.presentation.theme.DougScoreTheme

@OptIn(ExperimentalComposeUiApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

	@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
	override fun onCreate(savedInstanceState: Bundle?) {
		val splashScreen = installSplashScreen()
		enableEdgeToEdge()
		super.onCreate(savedInstanceState)
		val settingsViewModel by viewModels<SettingsViewModel>()
		val overviewViewModel by viewModels<OverviewViewModel>()

		splashScreen.setKeepOnScreenCondition {
			settingsViewModel.settingsState.value.initialState
		}

		setContent {
			val settingsState by settingsViewModel.settingsState.collectAsStateWithLifecycle()
			val overviewState by overviewViewModel.state.collectAsStateWithLifecycle()
			val hasActiveFilters = overviewState.selectedManufacturers.isNotEmpty() ||
					overviewState.selectedCountries.isNotEmpty()
			/*
			 * Should use dark theme when system is dark and user wants to follow the system theme
			 * OR
			 * dark theme is enabled AND follow device theme is disabled
			 */
			val shouldUseDarkTheme =
				(isSystemInDarkTheme() && settingsState.useDeviceTheme) ||
						settingsState.useDarkTheme && !settingsState.useDeviceTheme

			DougScoreTheme(
				darkTheme = shouldUseDarkTheme,
				dynamicColor = settingsState.useDynamicColor
			) {
				val scrollBehavior =
					TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
				val navController = rememberNavController()
				val sheetState = rememberModalBottomSheetState()
				var isSheetOpen by rememberSaveable { mutableStateOf(false) }
				val currentDestination =
					navController.currentBackStackEntryAsState().value?.destination?.route

				Scaffold(
					topBar = {
						CenterAlignedTopAppBar(
							navigationIcon = {
								AnimatedBackButton(
									currentDestination,
									navController
								)
							},
							title = {
								Text(
									text = stringResource(R.string.app_header),
									maxLines = 1,
									overflow = TextOverflow.Ellipsis
								)
							},
							actions = {
								if (currentDestination?.endsWith(Route.OverView.javaClass.simpleName) != false) {
									IconButton(onClick = {
										isSheetOpen = true
									}) {
										BadgedBox(
											badge = { if (hasActiveFilters) Badge() }
										) {
											Icon(
												imageVector = Icons.Default.Search,
												contentDescription = stringResource(R.string.open_filters_content_description)
											)
										}
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
							startDestination = Route.OverView,
						) {
							composable<Route.OverView> {
								OverviewScreenRoot(
									overviewViewModel = overviewViewModel,
									onCarClick = { carId ->
										navController.navigate(Route.Details(carId))
									},
									onDescriptionClick = {
										navController.navigate(Route.Description)
									},
									animatedVisibilityScope = this@composable,
								)
								if (isSheetOpen) {
									ModalBottomSheet(
										sheetState = sheetState,
										onDismissRequest = { isSheetOpen = false }
									) {
										BottomSheetContent(
											searchText = overviewState.searchQuery,
											isDescendingOrder = overviewState.isDescending,
											availableManufacturers = overviewState.availableManufacturers,
											selectedManufacturers = overviewState.selectedManufacturers,
											availableCountries = overviewState.availableCountries,
											selectedCountries = overviewState.selectedCountries,
											onAction = overviewViewModel::onAction,
										)
									}
								}
							}
							composable<Route.Details> {
								DetailsScreen(
									animatedVisibilityScope = this@composable,
								)
							}
							composable<Route.Settings> {
								SettingsScreen(
									lastRefreshTimeInMillis = settingsState.lastUpdatedTimeStamp,
									isLoading = settingsState.isLoading,
									settingsState = settingsState,
									onAction = settingsViewModel::onAction,
								)
							}
							composable<Route.Description> {
								DescriptionScreen()
							}
						}
					}
				}
			}
		}
	}
}