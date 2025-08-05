package com.rhysstever.restaurantorders.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rhysstever.restaurantorders.R
import com.rhysstever.restaurantorders.ui.Order
import com.rhysstever.restaurantorders.ui.RestaurantViewModel
import com.rhysstever.restaurantorders.ui.components.ButtonFill
import com.rhysstever.restaurantorders.ui.components.RatingsRow
import com.rhysstever.restaurantorders.ui.components.ScreenScaffold
import com.rhysstever.restaurantorders.ui.components.StyledTextField
import com.rhysstever.restaurantorders.ui.navigation.AddOrder
import com.rhysstever.restaurantorders.ui.navigation.VisitInfo
import com.rhysstever.restaurantorders.ui.theme.Typography

@Composable
fun AddOrderScreen(
    navController: NavController,
    restaurantViewModel: RestaurantViewModel
) {
    val uiState by restaurantViewModel.uiState.collectAsState()

    ScreenScaffold(
        currentScreen = AddOrder,
        onBack = { navController.navigate(VisitInfo.route) },
    ) { innerPadding ->
        uiState.selectedRestaurant?.let { _ ->
            uiState.selectedVisit?.let { _ ->
                AddOrderScreenContent(
                    isOrderNameInputInvalid = uiState.isNewOrderInputInvalid,
                    onNewOrderInput = { newOrderName ->
                        restaurantViewModel.OrderContent().checkNewOrderInput(newOrderName)
                    },
                    onKeyboardDone = { newOrderName ->
                        restaurantViewModel.OrderContent().checkNewOrderInput(newOrderName)
                    },
                    onAddNewOrder = { order ->
                        restaurantViewModel.OrderContent().addNewOrder(order)
                        navController.navigate(VisitInfo.route)
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            } ?: NoSelectedVisitMessage(modifier = Modifier.padding(innerPadding))
        } ?: NoSelectedRestaurantMessage(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
private fun AddOrderScreenContent(
    isOrderNameInputInvalid: Boolean?,
    onNewOrderInput: (String) -> Unit,
    onKeyboardDone: (String) -> Unit,
    onAddNewOrder: (Order) -> Unit,
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
            ratingTitle = stringResource(R.string.order_rating),
            arrangement = Arrangement.Start
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
        ButtonFill(
            text = stringResource(R.string.add_order),
            onClick = {
                // Create a new Order object
                val newOrder = Order(
                    name = orderName,
                    rating = rating,
                    notes = notes,
                )

                // Add the new order to the visit of the restaurant
                onAddNewOrder(newOrder)
            },
            enabled = isOrderNameInputInvalid?.let { !it } ?: false,
        )
    }
}

@Composable
private fun NoSelectedVisitMessage(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .semantics(mergeDescendants = true) {},
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.no_visit_text),
            textAlign = TextAlign.Center,
            style = Typography.bodyLarge
        )
    }
}

@Preview
@Composable
private fun AddOrderScreenNoSelectionPreview() {
    AddOrderScreenContent(
        isOrderNameInputInvalid = false,
        onNewOrderInput = { },
        onKeyboardDone = { },
        onAddNewOrder = { _ -> },
    )
}