package com.rhysstever.restaurantorders.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rhysstever.restaurantorders.AddRestaurant
import com.rhysstever.restaurantorders.R
import com.rhysstever.restaurantorders.ui.RestaurantUIState
import com.rhysstever.restaurantorders.ui.components.ScreenScaffold
import com.rhysstever.restaurantorders.ui.components.StyledTextField
import com.rhysstever.restaurantorders.ui.demoUIState

@Composable
fun AddRestaurantScreen(
    state: RestaurantUIState,
    onBack: () -> Unit,
    checkRestaurantInput: (String) -> Unit,
    onAddNewRestaurant: (String) -> Unit,
) {
    ScreenScaffold(
        currentScreen = AddRestaurant,
        onBack = onBack,
        onAdd = null,
    ) { innerPadding ->
        AddRestaurantScreenContent(
            isInputInvalid = state.isNewRestaurantInputInvalid,
            checkRestaurantInput = checkRestaurantInput,
            onAddNewRestaurant = onAddNewRestaurant,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun AddRestaurantScreenContent(
    isInputInvalid: Boolean?,
    checkRestaurantInput: (String) -> Unit,
    onAddNewRestaurant: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val (restaurantName, onRestaurantNameChange) = remember { mutableStateOf("") }

    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StyledTextField(
            value = restaurantName,
            onValueChange = { newName ->
                onRestaurantNameChange(newName)
                checkRestaurantInput(newName)
            },
            isInputInvalid = isInputInvalid,
            label = isInputInvalid?.let { isInvalid ->
                if (isInvalid) {
                    if (restaurantName.isBlank()) {
                        stringResource(R.string.invalid_restaurant_name)
                    } else {
                        stringResource(R.string.restaurant_name_exists)
                    }
                } else {
                    stringResource(R.string.enter_restaurant_name)
                }
            } ?: stringResource(R.string.enter_restaurant_name),
            onKeyboardDone = {
                checkRestaurantInput(restaurantName)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { onAddNewRestaurant(restaurantName) },
            enabled = isInputInvalid?.let { !it } ?: false,
        ) {
            Text(text = stringResource(R.string.add_restaurant))
        }
    }
}

@Preview
@Composable
private fun AddRestaurantScreenPreview() {
    AddRestaurantScreen(
        state = demoUIState.copy(selectedRestaurant = demoUIState.restaurants[0]),
        onBack = {},
        checkRestaurantInput = {},
        onAddNewRestaurant = {}
    )
}