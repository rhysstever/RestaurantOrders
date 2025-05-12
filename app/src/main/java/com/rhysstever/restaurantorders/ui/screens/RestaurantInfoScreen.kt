package com.rhysstever.restaurantorders.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rhysstever.restaurantorders.Orders
import com.rhysstever.restaurantorders.ui.Order
import com.rhysstever.restaurantorders.ui.Restaurant
import com.rhysstever.restaurantorders.ui.RestaurantViewModel
import com.rhysstever.restaurantorders.ui.components.AccessibleIcon
import com.rhysstever.restaurantorders.ui.components.ScreenScaffold
import com.rhysstever.restaurantorders.ui.theme.Typography

@Composable
fun RestaurantInfoScreen(
    navController: NavHostController = rememberNavController(),
    restaurantViewModel: RestaurantViewModel = viewModel()
) {
    val restaurantUIState by restaurantViewModel.uiState.collectAsState()

    ScreenScaffold(
        currentScreen = Orders,
        navController = navController,
        restaurantViewModel = restaurantViewModel
    ) { innerPadding ->
        restaurantUIState.selectedRestaurant?.let {
            RestaurantScreenContent(
                restaurant = it,
                onFavoriteClick = { restaurant ->
                    restaurantViewModel.toggleRestaurantIsFavorite(restaurant)
                },
                orderName = restaurantViewModel.newOrderInput,
                isOrderNameInputInvalid = restaurantUIState.isNewOrderInputInvalid,
                onNewOrderInput = { newOrderName ->
                    restaurantViewModel.updateNewOrderInput(newOrderName)
                },
                onKeyboardDone = { restaurantViewModel.checkNewOrderInput() },
                onAddNewOrder = { restaurant, order ->
                    restaurantViewModel.addNewOrder(restaurant, order)
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
    orderName: String,
    isOrderNameInputInvalid: Boolean?,
    onNewOrderInput: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    onAddNewOrder: (Restaurant, Order) -> Unit,
    modifier: Modifier = Modifier
) {
    var isOrderDialogShowing by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
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
            onClick = {
                onNewOrderInput("")
                isOrderDialogShowing = true
            }
        ) {
            Text(text = "Add Order")
        }

        Column {
            restaurant.orders.forEach {
                OrderListItem(order = it)
            }
        }

        AddOrderDialog(
            isShowing = isOrderDialogShowing,
            onDismiss = { isOrderDialogShowing = false },
            restaurant = restaurant,
            orderName = orderName,
            isOrderNameInputInvalid = isOrderNameInputInvalid,
            onNewOrderInput = onNewOrderInput,
            onKeyboardDone = onKeyboardDone,
            onAddNewOrder = onAddNewOrder
        )
    }
}

@Composable
fun AddOrderDialog(
    isShowing: Boolean,
    onDismiss: () -> Unit,
    restaurant: Restaurant,
    orderName: String,
    isOrderNameInputInvalid: Boolean?,
    onNewOrderInput: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    onAddNewOrder: (Restaurant, Order) -> Unit,
) {
    if(isShowing) {
        val (notes, onNotesValueChange) = remember { mutableStateOf("") }

        Dialog(
            onDismissRequest = onDismiss
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Add New Order")
                OutlinedTextField(
                    value = orderName,
                    onValueChange = onNewOrderInput,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        isOrderNameInputInvalid?.let { isInvalid ->
                            if(isInvalid) {
                                if(orderName.isBlank()) {
                                    Text(text = "Invalid Order Name")
                                } else {
                                    Text(text = "Order Name Already Exists")
                                }
                            } else {
                                Text(text = "Order Restaurant Name")
                            }
                        } ?: Text(text = "Order Restaurant Name")
                    },
                    isError = isOrderNameInputInvalid ?: false,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onKeyboardDone() }
                    )
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = onNotesValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Add any notes") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onKeyboardDone() }
                    )
                )
                Button(
                    onClick = {
                        // Create a new Order object
                        val newOrder = Order(
                            name = orderName,
                            rating = 5, // Default rating
                            notes = notes
                        )

                        // Add the new order to the restaurant
                        onAddNewOrder(restaurant, newOrder)

                        // Close the dialog
                        onDismiss()
                    },
                    enabled = isOrderNameInputInvalid?.let { !it } ?: false,
                ) {
                    Text(text = "Add Order")
                }
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