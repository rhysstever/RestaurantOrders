package com.rhysstever.restaurantorders.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rhysstever.restaurantorders.R
import com.rhysstever.restaurantorders.ui.RestaurantViewModel
import com.rhysstever.restaurantorders.ui.components.ScreenScaffold
import com.rhysstever.restaurantorders.ui.components.StyledTextField
import com.rhysstever.restaurantorders.ui.navigation.AddRestaurant
import com.rhysstever.restaurantorders.ui.navigation.Home

@Composable
fun AddRestaurantScreen(
    navController: NavController,
    restaurantViewModel: RestaurantViewModel
) {
    val uiState by restaurantViewModel.uiState.collectAsState()

    ScreenScaffold(
        currentScreen = AddRestaurant,
        onBack = { navController.navigate(Home.route) },
    ) { innerPadding ->
        AddRestaurantScreenContent(
            isInputInvalid = uiState.isNewRestaurantInputInvalid,
            checkRestaurantInput = { restaurantName ->
                restaurantViewModel.RestaurantContent().checkNewRestaurantInput(restaurantName)
            },
            onAddNewRestaurant = { restaurantName ->
                restaurantViewModel.RestaurantContent().addRestaurant(restaurantName)
                navController.navigate(Home.route)
            },
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
    AddRestaurantScreenContent(
        isInputInvalid = false,
        checkRestaurantInput = {},
        onAddNewRestaurant = {}
    )
}