package com.rhysstever.restaurantorders.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rhysstever.restaurantorders.Add
import com.rhysstever.restaurantorders.Home
import com.rhysstever.restaurantorders.Orders
import com.rhysstever.restaurantorders.ui.screens.AddRestaurantScreen
import com.rhysstever.restaurantorders.ui.screens.HomeScreen
import com.rhysstever.restaurantorders.ui.screens.RestaurantInfoScreen

@Composable
fun RestaurantNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val restaurantViewModel: RestaurantViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            HomeScreen(
                navController = navController,
                restaurantViewModel = restaurantViewModel
            )
        }
        composable(route = Add.route) {
            AddRestaurantScreen(
                navController = navController,
                restaurantViewModel = restaurantViewModel
            )
        }
        composable(route = Orders.route) {
            RestaurantInfoScreen(
                navController = navController,
                restaurantViewModel = restaurantViewModel
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }