package com.rhysstever.restaurantorders.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.rhysstever.restaurantorders.AddOrder
import com.rhysstever.restaurantorders.AddRestaurant
import com.rhysstever.restaurantorders.Home
import com.rhysstever.restaurantorders.RestaurantDestination
import com.rhysstever.restaurantorders.RestaurantInfo

@Composable
fun ScreenScaffold(
    currentScreen: RestaurantDestination,
    navController: NavHostController,
    updateNewRestaurantInput: (String) -> Unit,
    updateNewOrderInput: (String) -> Unit,
    isOnlyShowingFavorites: Boolean,
    onToggleShowFavorites: () -> Unit,
    contentToShow: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RestaurantTopAppBar(
                currentScreen = currentScreen,
                onBack = when(currentScreen) {
                    Home -> null
                    AddRestaurant -> { { navController.navigate(Home.route) } }
                    RestaurantInfo -> { { navController.navigate(Home.route) } }
                    AddOrder -> { { navController.navigate(RestaurantInfo.route) } }
                },
                onAdd = when(currentScreen) {
                    Home -> { {
                        updateNewRestaurantInput("")
                        navController.navigate(AddRestaurant.route)
                    } }
                    RestaurantInfo -> { {
                        updateNewOrderInput("")
                        navController.navigate(AddOrder.route)
                    } }
                    else -> null
                },
                onlyShowFavorites = if(currentScreen == Home) {
                    Pair(isOnlyShowingFavorites) {
                        onToggleShowFavorites()
                    }
                } else { null }
            )
        }
    ) { innerPadding ->
        contentToShow(innerPadding)
    }
}

@Composable
fun ScreenScaffold(
    currentScreen: RestaurantDestination,
    navController: NavHostController,
    updateNewRestaurantInput: (String) -> Unit,
    updateNewOrderInput: (String) -> Unit,
    contentToShow: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RestaurantTopAppBar(
                currentScreen = currentScreen,
                onBack = when(currentScreen) {
                    Home -> null
                    AddRestaurant -> { { navController.navigate(Home.route) } }
                    RestaurantInfo -> { { navController.navigate(Home.route) } }
                    AddOrder -> { { navController.navigate(RestaurantInfo.route) } }
                },
                onAdd = when(currentScreen) {
                    Home -> { {
                        updateNewRestaurantInput("")
                        navController.navigate(AddRestaurant.route)
                    } }
                    RestaurantInfo -> { {
                        updateNewOrderInput("")
                        navController.navigate(AddOrder.route)
                    } }
                    else -> null
                },
                onlyShowFavorites = null
            )
        }
    ) { innerPadding ->
        contentToShow(innerPadding)
    }
}