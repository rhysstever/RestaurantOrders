package com.rhysstever.restaurantorders.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rhysstever.restaurantorders.Add
import com.rhysstever.restaurantorders.Home
import com.rhysstever.restaurantorders.ui.RestaurantViewModel
import com.rhysstever.restaurantorders.ui.components.RestaurantBottomTabRow
import com.rhysstever.restaurantorders.ui.components.RestaurantTopAppBar
import com.rhysstever.restaurantorders.ui.navigateSingleTopTo

@Composable
fun AddRestaurantScreen(
    navController: NavHostController = rememberNavController(),
    restaurantViewModel: RestaurantViewModel = viewModel()
) {
    val restaurantUIState by restaurantViewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RestaurantTopAppBar(
                currentScreen = Add,
                onBack = navController.previousBackStackEntry?.let {
                    { navController.popBackStack() }
                },
            )
        },
        bottomBar = {
            RestaurantBottomTabRow(
                onTabSelected = { newScreen ->
                    if(newScreen == Add) {
                        restaurantViewModel.updateNewRestaurantInput("")
                    }

                    navController.navigateSingleTopTo(newScreen.route)
                },
                currentScreen = Add
            )
        }
    ) { innerPadding ->
        AddRestaurantScreenContent(
            restaurantName = restaurantViewModel.newRestaurantInput,
            isInputInvalid = restaurantUIState.isNewRestaurantInputInvalid,
            onNewRestaurantInput = { newRestaurantName ->
                restaurantViewModel.updateNewRestaurantInput(newRestaurantName)
            },
            onKeyboardDone = { restaurantViewModel.checkNewRestaurantInput() },
            onAddRestaurant = {
                restaurantViewModel.addRestaurant()
                navController.navigateSingleTopTo(Home.route)
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun AddRestaurantScreenContent(
    restaurantName: String,
    isInputInvalid: Boolean?,
    onNewRestaurantInput: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    onAddRestaurant: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = restaurantName,
            onValueChange = onNewRestaurantInput,
            modifier = Modifier.fillMaxWidth(),
            label = {
                isInputInvalid?.let { isInvalid ->
                    if(isInvalid) {
                        if(restaurantName.isBlank()) {
                            Text(text = "Invalid Restaurant Name")
                        } else {
                            Text(text = "Restaurant Name Already Exists")
                        }
                    } else {
                        Text(text = "Enter Restaurant Name")
                    }
                } ?: Text(text = "Enter Restaurant Name")
            },
            isError = isInputInvalid ?: false,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onKeyboardDone() }
            )
        )

        Button(
            onClick = onAddRestaurant,
            enabled = isInputInvalid?.let { !it } ?: false,
        ) {
            Text("Add Restaurant")
        }
    }
}

@Preview
@Composable
fun AddRestaurantScreenPreview() {
    AddRestaurantScreen()
}