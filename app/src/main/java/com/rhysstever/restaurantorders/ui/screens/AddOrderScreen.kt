package com.rhysstever.restaurantorders.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rhysstever.restaurantorders.AddOrder
import com.rhysstever.restaurantorders.R
import com.rhysstever.restaurantorders.RestaurantInfo
import com.rhysstever.restaurantorders.ui.Order
import com.rhysstever.restaurantorders.ui.Restaurant
import com.rhysstever.restaurantorders.ui.RestaurantViewModel
import com.rhysstever.restaurantorders.ui.components.RatingsRow
import com.rhysstever.restaurantorders.ui.components.ScreenScaffold
import com.rhysstever.restaurantorders.ui.components.StyledTextField

@Composable
fun AddOrderScreen(
    navController: NavHostController = rememberNavController(),
    restaurantViewModel: RestaurantViewModel = viewModel()
) {
    val restaurantUIState by restaurantViewModel.uiState.collectAsState()

    ScreenScaffold(
        currentScreen = AddOrder,
        onBack = { navController.navigate(RestaurantInfo.route) },
        onAdd = null,
    ) { innerPadding ->
        AddOrderScreenContent(
            restaurant = restaurantUIState.selectedRestaurant!!,
            orderName = restaurantViewModel.newOrderInput,
            isOrderNameInputInvalid = restaurantUIState.isNewOrderInputInvalid,
            onNewOrderInput = { newOrderName ->
                restaurantViewModel.OrderContent().updateNewOrderInput(newOrderName)
            },
            onKeyboardDone = { restaurantViewModel.OrderContent().checkNewOrderInput() },
            onAddNewOrder = { restaurant, order ->
                restaurantViewModel.OrderContent().addNewOrder(restaurant, order)
                navController.navigate(RestaurantInfo.route)
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        // Order name text field
        StyledTextField(
            value = orderName,
            onValueChange = onNewOrderInput,
            isInputInvalid = isOrderNameInputInvalid,
            label = isOrderNameInputInvalid?.let { isInvalid ->
                if (isInvalid) {
                    if (orderName.isBlank()) {
                        stringResource(R.string.invalid_order_name)
                    } else {
                        stringResource(R.string.order_name_exists)
                    }
                } else {
                    stringResource(R.string.enter_order_name)
                }
            } ?: stringResource(R.string.enter_order_name),
            onKeyboardDone = onKeyboardDone,
            modifier = Modifier.fillMaxWidth()
        )

        RatingsRow(
            rating = rating,
            onRatingChanged = {
                rating = if(it == rating) { 0 } else { it }
            }
        )

        // Notes text field
        StyledTextField(
            value = notes,
            onValueChange = onNotesValueChange,
            isInputInvalid = isOrderNameInputInvalid,
            label = stringResource(R.string.add_notes),
            onKeyboardDone = onKeyboardDone,
            modifier = Modifier.fillMaxWidth()
        )
        
        // TODO: Uncomment when date picker is implemented
//        val dateOrderedState = rememberDatePickerState()
//
//        CustomDatePicker(
//            state = dateOrderedState,
//            headlineText = stringResource(R.string.order_date_picker_header)
//        )

        // Submit button
        Button(
            onClick = {
                // Create a new Order object
                val newOrder = Order(
                    name = orderName,
                    rating = rating,
                    notes = notes,
                    dateCreated = java.time.LocalDate.now(),
                    dateOrdered = null
                )

                // Add the new order to the restaurant
                onAddNewOrder(restaurant, newOrder)
            },
            enabled = isOrderNameInputInvalid?.let { !it } ?: false,
        ) {
            Text(text = stringResource(R.string.add_order))
        }
    }
}

@Preview
@Composable
fun AddOrderScreenPreview() {
    AddOrderScreen()
}