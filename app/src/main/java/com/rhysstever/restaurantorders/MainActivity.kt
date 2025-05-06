package com.rhysstever.restaurantorders

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rhysstever.restaurantorders.ui.RestaurantNavHost
import com.rhysstever.restaurantorders.ui.theme.RestaurantOrdersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RestaurantOrdersApp()
        }
    }
}

@Composable
fun RestaurantOrdersApp() {
    RestaurantOrdersTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = restaurantTabRowScreens.find { it.route == currentDestination?.route } ?: Home

        RestaurantNavHost(navController = navController)
    }
}