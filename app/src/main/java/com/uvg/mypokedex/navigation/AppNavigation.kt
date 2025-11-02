package com.uvg.mypokedex.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uvg.mypokedex.ui.detail.DetailScreen
import com.uvg.mypokedex.ui.detail.DetailViewModel
import com.uvg.mypokedex.ui.detail.DetailViewModelFactory
import com.uvg.mypokedex.ui.features.home.HomeScreen
import com.uvg.mypokedex.ui.features.home.HomeViewModel
import com.uvg.mypokedex.ui.features.home.HomeViewModelFactory

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.Home.route
    ) {
        composable(route = AppScreens.Home.route) {
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(
                    application = androidx.compose.ui.platform.LocalContext.current.applicationContext as android.app.Application
                )
            )

            HomeScreen(
                viewModel = viewModel,
                onPokemonClick = { pokemonId ->
                    navController.navigate(AppScreens.Detail.createRoute(pokemonId))
                }
            )
        }

        composable(
            route = AppScreens.Detail.route,
            arguments = listOf(
                navArgument("pokemonId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: 1

            val viewModel: DetailViewModel = viewModel(
                factory = DetailViewModelFactory(
                    application = androidx.compose.ui.platform.LocalContext.current.applicationContext as android.app.Application
                )
            )

            DetailScreen(
                pokemonId = pokemonId,
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
