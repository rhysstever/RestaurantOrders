package com.rhysstever.restaurantorders

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface RestaurantDestination {
    val selectedIcon: ImageVector
    val unselectedIcon: ImageVector
    val route: String
}

data object Home : RestaurantDestination {
    override val selectedIcon = Icons.Default.Home
    override val unselectedIcon = Icons.Outlined.Home
    override val route = "Home"
}

data object Add : RestaurantDestination {
    override val selectedIcon = Icons.Default.AddCircle
    override val unselectedIcon = Icons.Default.Add
    override val route = "Add"
}

data object Orders : RestaurantDestination {
    override val selectedIcon = Icons.Default.Info
    override val unselectedIcon = Icons.Outlined.Info
    override val route = "Orders"
}

val restaurantTabRowScreens = listOf(Home, Add, Orders)