package com.rhysstever.restaurantorders

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rhysstever.restaurantorders.ui.RestaurantViewModel
import com.rhysstever.restaurantorders.ui.screens.AddOrderScreen
import com.rhysstever.restaurantorders.ui.screens.AddRestaurantScreen
import com.rhysstever.restaurantorders.ui.screens.HomeScreen
import com.rhysstever.restaurantorders.ui.screens.RestaurantInfoScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RestaurantNavHost(navController = rememberNavController())
        }
    }
}

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
        composable(route = AddRestaurant.route) {
            AddRestaurantScreen(
                navController = navController,
                restaurantViewModel = restaurantViewModel
            )
        }
        composable(route = RestaurantInfo.route) {
            RestaurantInfoScreen(
                navController = navController,
                restaurantViewModel = restaurantViewModel
            )
        }
        composable(route = AddOrder.route) {
            AddOrderScreen(
                navController = navController,
                restaurantViewModel = restaurantViewModel
            )
        }
    }
}

