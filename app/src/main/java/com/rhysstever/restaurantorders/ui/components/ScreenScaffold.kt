package com.rhysstever.restaurantorders.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.rhysstever.restaurantorders.AddRestaurant
import com.rhysstever.restaurantorders.Home
import com.rhysstever.restaurantorders.RestaurantDestination
import com.rhysstever.restaurantorders.navigateSingleTopTo
import com.rhysstever.restaurantorders.ui.RestaurantViewModel

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
                onAdd = if(currentScreen != AddRestaurant) { {
                    restaurantViewModel.updateNewRestaurantInput("")
                    navController.navigateSingleTopTo(AddRestaurant.route)
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
        }
    ) { innerPadding ->
        contentToShow(innerPadding)
    }
}