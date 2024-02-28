package gabor.koleszar.dougscore.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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

		enableEdgeToEdge()

		val mainViewModel by viewModels<MainViewModel>()
		splashScreen.setKeepOnScreenCondition {
			mainViewModel.isLoading
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
							val cars by mainViewModel.cars.collectAsState()
							OverviewScreen(
								onCarClick = { carId ->
									mainViewModel.onCarSelected(carId)
									navController.navigate(Route.DETAILS)
								},
								onPullRefresh = mainViewModel::refresh,
								onSearchTextChanged = mainViewModel::onSearchTextChange,
								cars = cars,
								isLoading = mainViewModel.isLoading,
								isRefreshing = mainViewModel.isRefreshing,
								searchText = mainViewModel.searchText.collectAsState().value
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