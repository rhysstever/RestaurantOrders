package com.rhysstever.restaurantorders.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rhysstever.restaurantorders.AddOrder
import com.rhysstever.restaurantorders.RestaurantInfo
import com.rhysstever.restaurantorders.navigateSingleTopTo
import com.rhysstever.restaurantorders.ui.Order
import com.rhysstever.restaurantorders.ui.Restaurant
import com.rhysstever.restaurantorders.ui.RestaurantViewModel
import com.rhysstever.restaurantorders.ui.components.AccessibleIcon
import com.rhysstever.restaurantorders.ui.components.ScreenScaffold

@Composable
fun RestaurantInfoScreen(
    navController: NavHostController = rememberNavController(),
    restaurantViewModel: RestaurantViewModel = viewModel()
) {
    val restaurantUIState by restaurantViewModel.uiState.collectAsState()

    ScreenScaffold(
        currentScreen = RestaurantInfo,
        navController = navController,
        restaurantViewModel = restaurantViewModel
    ) { innerPadding ->
        restaurantUIState.selectedRestaurant?.let {
            RestaurantScreenContent(
                restaurant = it,
                onFavoriteClick = { restaurant ->
                    restaurantViewModel.toggleRestaurantIsFavorite(restaurant)
                },
                onAddNewOrder = {
                    restaurantViewModel.updateNewOrderInput("")
                    navController.navigateSingleTopTo(AddOrder.route)
                },
                modifier = Modifier.padding(innerPadding)
            )
        } ?: run {
            NoRestaurantList(modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun RestaurantScreenContent(
    restaurant: Restaurant,
    onFavoriteClick: (Restaurant) -> Unit,
    onAddNewOrder: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = restaurant.name)
            AccessibleIcon(
                imageVector = if(restaurant.isFavorite) {
                    Icons.Default.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                contentDescription = "Favorite",
                onClick = { onFavoriteClick(restaurant) }
            )
        }

        Button(
            onClick = onAddNewOrder
        ) { Text(text = "Add Order") }

        Column {
            Text("Orders")
            restaurant.orders.reversed().forEachIndexed { index, order ->
                // If it is the first order to list or its the first order of a new rating,
                // show a rating heading
                if(index == 0 || index > 0 && restaurant.orders.reversed()[index - 1].rating != order.rating) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (order.rating != 1) {
                            Text("${order.rating} Stars")
                        } else {
                            Text("${order.rating} Star")
                        }
                        Spacer(modifier = modifier.width(4.dp))
                        // Display a row of stars for the rating
                        repeat(order.rating) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                OrderListItem(order = order)
            }
        }
    }
}

@Composable
fun OrderListItem(
    order: Order
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = order.name)
        Text(text = order.notes)
    }
}

@Preview
@Composable
fun RestaurantScreenPreview() {
    RestaurantInfoScreen()
}