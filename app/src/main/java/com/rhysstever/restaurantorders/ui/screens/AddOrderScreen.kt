package com.rhysstever.restaurantorders.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rhysstever.restaurantorders.AddOrder
import com.rhysstever.restaurantorders.R
import com.rhysstever.restaurantorders.ui.Order
import com.rhysstever.restaurantorders.ui.Restaurant
import com.rhysstever.restaurantorders.ui.RestaurantUIState
import com.rhysstever.restaurantorders.ui.components.CustomDatePicker
import com.rhysstever.restaurantorders.ui.components.RatingsRow
import com.rhysstever.restaurantorders.ui.components.ScreenScaffold
import com.rhysstever.restaurantorders.ui.components.StyledTextField
import com.rhysstever.restaurantorders.ui.components.convertMillisToDate
import com.rhysstever.restaurantorders.ui.demoUIState
import com.rhysstever.restaurantorders.ui.demoUIStateSelected
import com.rhysstever.restaurantorders.ui.theme.Typography

@Composable
fun AddOrderScreen(
    state: RestaurantUIState,
    onBack: () -> Unit,
    onNewOrderInput: (String) -> Unit,
    onKeyboardDone: (String) -> Unit,
    onAddNewOrder: (Restaurant, Order) -> Unit,
) {
    ScreenScaffold(
        currentScreen = AddOrder,
        onBack = onBack,
        onAdd = null,
    ) { innerPadding ->
        state.selectedRestaurant?.let { currentlySelectedRestaurant ->
            AddOrderScreenContent(
                restaurant = currentlySelectedRestaurant,
                isOrderNameInputInvalid = state.isNewOrderInputInvalid,
                onNewOrderInput = onNewOrderInput,
                onKeyboardDone = onKeyboardDone,
                onAddNewOrder = onAddNewOrder,
                modifier = Modifier.padding(innerPadding)
            )
        } ?: NoSelectedRestaurantMessage(modifier = Modifier.padding(innerPadding))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddOrderScreenContent(
    restaurant: Restaurant,
    isOrderNameInputInvalid: Boolean?,
    onNewOrderInput: (String) -> Unit,
    onKeyboardDone: (String) -> Unit,
    onAddNewOrder: (Restaurant, Order) -> Unit,
    modifier: Modifier = Modifier
) {
    val (orderName, onOrderNameChange) = remember { mutableStateOf("") }
    val (notes, onNotesValueChange) = remember { mutableStateOf("") }
    var rating by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Order name text field
        StyledTextField(
            value = orderName,
            onValueChange = { newOrderName ->
                onOrderNameChange(newOrderName)
                onNewOrderInput(newOrderName)
            },
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
            onKeyboardDone = { onKeyboardDone(orderName) },
            modifier = Modifier.fillMaxWidth()
        )

        RatingsRow(
            rating = rating,
            onRatingChanged = {
                rating = if(it == rating) { null } else { it }
            },
            ratingTitle = stringResource(R.string.order_rating)
        )

        // Date ordered picker
        val dateOrderedState = rememberDatePickerState(
            initialSelectedDateMillis = null,
            initialDisplayMode = DisplayMode.Input,
        )
        CustomDatePicker(
            state = dateOrderedState,
            headlineText = stringResource(R.string.date_ordered_picker_header)
        )

        // Notes text field
        StyledTextField(
            value = notes,
            onValueChange = onNotesValueChange,
            isInputInvalid = false,
            label = stringResource(R.string.add_notes),
            onKeyboardDone = { },
            modifier = Modifier.fillMaxWidth()
        )

        // Submit button
        Button(
            onClick = {
                // Create a new Order object
                val newOrder = Order(
                    name = orderName,
                    rating = rating,
                    notes = notes,
                    dateOrdered = dateOrderedState.selectedDateMillis?.let {
                        convertMillisToDate(it)
                    }
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

@Composable
private fun NoSelectedRestaurantMessage(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.no_restaurant_text),
            textAlign = TextAlign.Center,
            style = Typography.bodyLarge
        )
    }
}

@Preview
@Composable
fun AddOrderScreenPreview() {
    AddOrderScreen(
        state = demoUIStateSelected,
        onBack = { },
        onNewOrderInput = { },
        onKeyboardDone = { },
        onAddNewOrder = { _, _ -> },
    )
}

@Preview
@Composable
fun AddOrderScreenNoSelectionPreview() {
    AddOrderScreen(
        state = demoUIState,
        onBack = { },
        onNewOrderInput = { },
        onKeyboardDone = { },
        onAddNewOrder = { _, _ -> },
    )
}