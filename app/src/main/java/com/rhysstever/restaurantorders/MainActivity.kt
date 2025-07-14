package com.rhysstever.restaurantorders

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
    val state = restaurantViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            HomeScreen(
                state = state.value,
                onAdd = {
                    navController.navigate(AddRestaurant.route)
                },
                onShowFavorites = {
                    restaurantViewModel.toggleShowingFavorites()
                },
                onRestaurantClicked = { restaurant ->
                    restaurantViewModel.RestaurantContent().selectRestaurant(restaurant)
                    navController.navigate(RestaurantInfo.route)
                }
            )
        }
        composable(route = AddRestaurant.route) {
            AddRestaurantScreen(
                state = state.value,
                onBack = { navController.navigate(Home.route) },
                checkRestaurantInput = { restaurantName ->
                    restaurantViewModel.RestaurantContent().checkNewRestaurantInput(restaurantName)
                },
                onAddNewRestaurant = { restaurantName ->
                    restaurantViewModel.RestaurantContent().addRestaurant(restaurantName)
                    navController.navigate(Home.route)
                }
            )
        }
        composable(route = RestaurantInfo.route) {
            RestaurantInfoScreen(
                state = state.value,
                onBack = {
                    restaurantViewModel.RestaurantContent().selectRestaurant(null)
                    navController.navigate(Home.route)
                },
                onAdd = {
                    navController.navigate(AddOrder.route)
                },
                onRestaurantRename = { newName ->
                    restaurantViewModel.RestaurantContent().renameRestaurant(newName)
                },
                onRestaurantNameValueChange = { newName ->
                    restaurantViewModel.RestaurantContent().checkRestaurantRenameInput(newName)
                },
                onFavoriteRestaurantClick = { restaurant ->
                    restaurantViewModel.RestaurantContent().toggleRestaurantIsFavorite(restaurant)
                },
                onKeyboardDone = {
                    restaurantViewModel.RestaurantContent().checkNewRestaurantInput("")
                },
                onRemoveOrder = { orderToRemove ->
                    restaurantViewModel.OrderContent().removeOrder(orderToRemove)
                },
            )
        }
        composable(route = AddOrder.route) {
            AddOrderScreen(
                state = state.value,
                onBack = { navController.navigate(RestaurantInfo.route) },
                isOrderNameInputInvalid = state.value.isNewOrderInputInvalid,
                onNewOrderInput = { newOrderName ->
                    restaurantViewModel.OrderContent().checkNewOrderInput(newOrderName)
                },
                onKeyboardDone = { newOrderName ->
                    restaurantViewModel.OrderContent().checkNewOrderInput(newOrderName)
                },
                onAddNewOrder = { restaurant, order ->
                    restaurantViewModel.OrderContent().addNewOrder(restaurant, order)
                    navController.navigate(RestaurantInfo.route)
                },
            )
        }
    }
}
