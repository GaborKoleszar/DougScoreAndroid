package gabor.koleszar.dougscore.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import gabor.koleszar.dougscore.presentation.details.DetailsScreen
import gabor.koleszar.dougscore.presentation.overview.MainViewModel
import gabor.koleszar.dougscore.presentation.overview.OverviewScreen
import gabor.koleszar.dougscore.presentation.theme.DougScoreTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		val splashScreen = installSplashScreen()
		super.onCreate(savedInstanceState)
		WindowCompat.setDecorFitsSystemWindows(window, false)
		val mainViewModel by viewModels<MainViewModel>()
		splashScreen.setKeepOnScreenCondition {
			mainViewModel.overviewState.isLoading
		}

		setContent {
			DougScoreTheme {
				val navController = rememberNavController()
				Scaffold(
					modifier = Modifier.fillMaxSize()
				) { scaffoldPadding ->
					NavHost(
						modifier = Modifier.padding(scaffoldPadding),
						navController = navController,
						startDestination = Route.OVERVIEW
					) {
						composable(
							route = Route.OVERVIEW
						) {
							OverviewScreen(
								onCarClick = { carId ->
									mainViewModel.detailsState = mainViewModel.detailsState.copy(
										car = mainViewModel.overviewState.cars[carId]
									)
									navController.navigate(Route.DETAILS)
								},
								onPullRefresh = { mainViewModel.refresh() },
								overviewState = mainViewModel.overviewState
							)
						}
						composable(route = Route.DETAILS) {
							DetailsScreen(
								detailsState = mainViewModel.detailsState
							)
						}
					}
				}
			}
		}
	}
}