package com.rhysstever.restaurantorders.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.rhysstever.restaurantorders.Add
import com.rhysstever.restaurantorders.Home
import com.rhysstever.restaurantorders.RestaurantDestination
import com.rhysstever.restaurantorders.ui.RestaurantViewModel
import com.rhysstever.restaurantorders.ui.navigateSingleTopTo

@Composable
fun ScreenScaffold(
    currentScreen: RestaurantDestination,
    navController: NavHostController,
    restaurantViewModel: RestaurantViewModel,
    contentToShow: @Composable (PaddingValues) -> Unit
) {
    val restaurantUIState by restaurantViewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RestaurantTopAppBar(
                currentScreen = currentScreen,
                onBack = navController.previousBackStackEntry?.let {
                    { navController.popBackStack() }
                },
                onAdd = if(currentScreen != Add) { {
                    restaurantViewModel.updateNewRestaurantInput("")
                    navController.navigateSingleTopTo(Add.route)
                } } else {
                    null
                },
                onlyShowFavorites = if(currentScreen == Home) {
                    Pair(
                        restaurantUIState.onlyShowFavorites
                    ) { restaurantViewModel.toggleShowingFavorites() }
                } else {
                    null
                }
            )
        },
        bottomBar = {
            RestaurantBottomTabRow(
                onTabSelected = { newScreen ->
                    if(newScreen == Add) {
                        restaurantViewModel.updateNewRestaurantInput("")
                    }

                    navController.navigateSingleTopTo(newScreen.route)
                },
                currentScreen = currentScreen
            )
        }
    ) { innerPadding ->
        contentToShow(innerPadding)
    }
}