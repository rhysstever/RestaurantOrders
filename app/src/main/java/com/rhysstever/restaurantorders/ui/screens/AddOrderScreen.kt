package com.rhysstever.restaurantorders.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rhysstever.restaurantorders.RestaurantInfo
import com.rhysstever.restaurantorders.navigateSingleTopTo
import com.rhysstever.restaurantorders.ui.Order
import com.rhysstever.restaurantorders.ui.Restaurant
import com.rhysstever.restaurantorders.ui.RestaurantViewModel
import com.rhysstever.restaurantorders.ui.components.RatingsRow
import com.rhysstever.restaurantorders.ui.components.ScreenScaffold

@Composable
fun AddOrderScreen(
    navController: NavHostController = rememberNavController(),
    restaurantViewModel: RestaurantViewModel = viewModel()
) {
    val restaurantUIState by restaurantViewModel.uiState.collectAsState()

    ScreenScaffold(
        currentScreen = RestaurantInfo,
        navController = navController,
        restaurantViewModel = restaurantViewModel
    ) { innerPadding ->
        AddOrderScreenContent(
            restaurant = restaurantUIState.selectedRestaurant!!,
            orderName = restaurantViewModel.newOrderInput,
            isOrderNameInputInvalid = restaurantUIState.isNewOrderInputInvalid,
            onNewOrderInput = { newOrderName ->
                restaurantViewModel.updateNewOrderInput(newOrderName)
            },
            onKeyboardDone = { restaurantViewModel.checkNewOrderInput() },
            onAddNewOrder = { restaurant, order ->
                restaurantViewModel.addNewOrder(restaurant, order)
                navController.navigateSingleTopTo(RestaurantInfo.route)
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun AddOrderScreenContent(
    restaurant: Restaurant,
    orderName: String,
    isOrderNameInputInvalid: Boolean?,
    onNewOrderInput: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    onAddNewOrder: (Restaurant, Order) -> Unit,
    modifier: Modifier = Modifier
) {
    val (notes, onNotesValueChange) = remember { mutableStateOf("") }
    var rating by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier.padding(horizontal = 16.dp),
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
                        Text(text = "Order Name")
                    }
                } ?: Text(text = "Order Name")
            },
            isError = isOrderNameInputInvalid ?: false,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onKeyboardDone() }
            )
        )

        RatingsRow(
            rating = rating,
            onRatingChanged = {
                rating = if(it == rating) {
                    0
                } else { it }
            }
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
                    rating = rating, // Default rating
                    notes = notes
                )

                // Add the new order to the restaurant
                onAddNewOrder(restaurant, newOrder)
            },
            enabled = isOrderNameInputInvalid?.let { !it } ?: false,
        ) {
            Text(text = "Add Order")
        }
    }
}

@Preview
@Composable
fun AddOrderScreenPreview() {
    AddOrderScreen()
}