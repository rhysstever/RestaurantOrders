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
import com.rhysstever.restaurantorders.ui.screens.AddVisitScreen
import com.rhysstever.restaurantorders.ui.screens.HomeScreen
import com.rhysstever.restaurantorders.ui.screens.RestaurantInfoScreen
import com.rhysstever.restaurantorders.ui.screens.VisitInfoScreen

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
                    navController.navigate(AddVisit.route)
                },
                onRestaurantRename = { newRestaurantName ->
                    restaurantViewModel.RestaurantContent().renameRestaurant(newRestaurantName)
                },
                onRestaurantNameValueChange = { newRestaurantName ->
                    restaurantViewModel.RestaurantContent().checkRestaurantRenameInput(newRestaurantName)
                },
                onFavoriteRestaurantClick = { restaurant ->
                    restaurantViewModel.RestaurantContent().toggleRestaurantIsFavorite(restaurant)
                },
                onKeyboardDone = { newRestaurantName ->
                    restaurantViewModel.RestaurantContent().checkNewRestaurantInput(newRestaurantName)
                },
                onVisitSelected = { selectedVisit ->
                    restaurantViewModel.VisitContent().selectVisit(selectedVisit)
                    navController.navigate(VisitInfo.route)
                },
                onRemoveVisit = { visitToRemove ->
                    restaurantViewModel.VisitContent().removeVisit(visitToRemove)
                },
            )
        }
        composable(route = AddVisit.route) {
            AddVisitScreen(
                state = state.value,
                onBack = { navController.navigate(RestaurantInfo.route) },
                onNewVisitInput = { newVisitName ->
                    restaurantViewModel.VisitContent().checkNewVisitInput(newVisitName)
                },
                onKeyboardDone = { newVisitName ->
                    restaurantViewModel.VisitContent().checkNewVisitInput(newVisitName)
                },
                onAddNewVisit = { restaurant, visit ->
                    restaurantViewModel.VisitContent().addVisit(restaurant, visit)
                    navController.navigate(RestaurantInfo.route)
                }
            )
        }
        composable(route = VisitInfo.route) {
            VisitInfoScreen(
                state = state.value,
                onBack = { navController.navigate(RestaurantInfo.route) },
                onAdd = { navController.navigate(AddOrder.route) },
                onEditVisit = {
                    navController.navigate(AddVisit.route)
                },
                onRemoveOrder = { orderToRemove ->
                    restaurantViewModel.OrderContent().removeOrder(orderToRemove)
                },
            )
        }
        composable(route = AddOrder.route) {
            AddOrderScreen(
                state = state.value,
                onBack = { navController.navigate(VisitInfo.route) },
                onNewOrderInput = { newOrderName ->
                    restaurantViewModel.OrderContent().checkNewOrderInput(newOrderName)
                },
                onKeyboardDone = { newOrderName ->
                    restaurantViewModel.OrderContent().checkNewOrderInput(newOrderName)
                },
                onAddNewOrder = { restaurant, visit, order ->
                    restaurantViewModel.OrderContent().addNewOrder(restaurant, visit, order)
                    navController.navigate(VisitInfo.route)
                },
            )
        }
    }
}
