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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rhysstever.restaurantorders.AddRestaurant
import com.rhysstever.restaurantorders.Home
import com.rhysstever.restaurantorders.R
import com.rhysstever.restaurantorders.ui.RestaurantViewModel
import com.rhysstever.restaurantorders.ui.components.ScreenScaffold
import com.rhysstever.restaurantorders.ui.components.StyledTextField

@Composable
fun AddRestaurantScreen(
    navController: NavHostController = rememberNavController(),
    restaurantViewModel: RestaurantViewModel = viewModel()
) {
    val restaurantUIState by restaurantViewModel.uiState.collectAsState()

    ScreenScaffold(
        currentScreen = AddRestaurant,
        onBack = { navController.navigate(Home.route) },
        onAdd = null,
    ) { innerPadding ->
        AddRestaurantScreenContent(
            restaurantName = restaurantViewModel.newRestaurantInput,
            onNewRestaurantInput = { newRestaurantName ->
                restaurantViewModel.RestaurantContent().updateNewRestaurantInput(newRestaurantName)
            },
            isInputInvalid = restaurantUIState.isNewRestaurantInputInvalid,
            onKeyboardDone = { restaurantViewModel.RestaurantContent().checkNewRestaurantInput() },
            onAddNewRestaurant = {
                restaurantViewModel.RestaurantContent().addRestaurant()
                navController.navigate(Home.route)
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun AddRestaurantScreenContent(
    restaurantName: String,
    onNewRestaurantInput: (String) -> Unit,
    isInputInvalid: Boolean?,
    onKeyboardDone: () -> Unit,
    onAddNewRestaurant: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StyledTextField(
            value = restaurantName,
            onValueChange = onNewRestaurantInput,
            isInputInvalid = isInputInvalid,
            label = {
                isInputInvalid?.let { isInvalid ->
                    if (isInvalid) {
                        if (restaurantName.isBlank()) {
                            Text(text = stringResource(R.string.invalid_restaurant_name))
                        } else {
                            Text(text = stringResource(R.string.restaurant_name_exists))
                        }
                    } else {
                        Text(text = stringResource(R.string.enter_restaurant_name))
                    }
                } ?: Text(text = stringResource(R.string.enter_restaurant_name))
            },
            onKeyboardDone = onKeyboardDone,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onAddNewRestaurant,
            enabled = isInputInvalid?.let { !it } ?: false,
        ) {
            Text(text = stringResource(R.string.add_restaurant))
        }
    }
}

@Preview
@Composable
fun AddRestaurantScreenPreview() {
    AddRestaurantScreen()
}