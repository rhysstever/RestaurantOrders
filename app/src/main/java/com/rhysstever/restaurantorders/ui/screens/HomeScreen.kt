package com.rhysstever.restaurantorders.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rhysstever.restaurantorders.Add
import com.rhysstever.restaurantorders.Home
import com.rhysstever.restaurantorders.ui.Restaurant
import com.rhysstever.restaurantorders.ui.RestaurantViewModel
import com.rhysstever.restaurantorders.ui.components.RestaurantBottomTabRow
import com.rhysstever.restaurantorders.ui.components.RestaurantTopAppBar
import com.rhysstever.restaurantorders.ui.navigateSingleTopTo

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    restaurantViewModel: RestaurantViewModel = viewModel()
) {
    val restaurantUIState by restaurantViewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RestaurantTopAppBar(
                currentScreen = Home,
                onBack = navController.previousBackStackEntry?.let {
                    { navController.popBackStack() }
                },
                onAdd = {
                    restaurantViewModel.updateNewRestaurantInput("")
                    navController.navigateSingleTopTo(Add.route)
                },
                onlyShowFavorites = Pair(
                    restaurantUIState.onlyShowFavorites
                ) { restaurantViewModel.toggleShowingFavorites() },
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
                currentScreen = Home,
            )
        }
    ) { innerPadding ->
        if(restaurantUIState.restaurants.isEmpty()) {
            NoRestaurantList(
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            // If there are restaurants to list, show the list
            // Filter the list down to only favorites if the toggle is on
            RestaurantList(
                restaurantList = if(restaurantUIState.onlyShowFavorites) {
                    restaurantUIState.restaurants.filter { it.isFavorite }
                } else {
                    restaurantUIState.restaurants
                },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun NoRestaurantList(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Add a restaurant to get started!")
    }
}

@Composable
fun RestaurantList(
    restaurantList: List<Restaurant>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        restaurantList.forEach { restaurant ->
            item {
                RestaurantListItem(restaurant = restaurant)
            }
        }
    }
}

@Composable
fun RestaurantListItem(
    restaurant: Restaurant
) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = if(restaurant.isFavorite) {
                Icons.Default.Favorite
            } else {
                Icons.Default.FavoriteBorder
            },
            contentDescription = "Favorite",
            modifier = Modifier.sizeIn(
                minWidth = 24.dp,
                minHeight = 24.dp
            )
        )
        Text(
            text = restaurant.name,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview
@Composable
fun RestaurantHomePreview() {
    HomeScreen()
}