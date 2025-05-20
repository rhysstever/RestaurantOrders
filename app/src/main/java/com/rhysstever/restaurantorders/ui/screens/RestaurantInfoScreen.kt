package com.rhysstever.restaurantorders.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rhysstever.restaurantorders.AddOrder
import com.rhysstever.restaurantorders.Home
import com.rhysstever.restaurantorders.R
import com.rhysstever.restaurantorders.RestaurantInfo
import com.rhysstever.restaurantorders.ui.Order
import com.rhysstever.restaurantorders.ui.Restaurant
import com.rhysstever.restaurantorders.ui.RestaurantViewModel
import com.rhysstever.restaurantorders.ui.components.AccessibleIcon
import com.rhysstever.restaurantorders.ui.components.CustomAlertDialog
import com.rhysstever.restaurantorders.ui.components.EditableHeader
import com.rhysstever.restaurantorders.ui.components.ScreenScaffold
import com.rhysstever.restaurantorders.ui.theme.Typography

@Composable
fun RestaurantInfoScreen(
    navController: NavHostController = rememberNavController(),
    restaurantViewModel: RestaurantViewModel = viewModel()
) {
    val restaurantUIState by restaurantViewModel.uiState.collectAsState()
    val isEditingRestaurantName = remember { mutableStateOf(false) }

//    Log.v("Rhys Test Restaurant Debug", "Is Editing: ${isEditingRestaurantName.value} Is Valid: ${restaurantUIState.isNewRestaurantInputInvalid}")

//    Log.v("Rhys Test Restaurant Debug", "Selected Res: ${restaurantUIState.selectedRestaurant}")

    ScreenScaffold(
        currentScreen = RestaurantInfo,
        onBack = {
            restaurantViewModel.RestaurantContent().selectRestaurant(null)
            navController.navigate(Home.route)
        },
        onAdd = {
            restaurantViewModel.OrderContent().updateNewOrderInput("")
            navController.navigate(AddOrder.route)
        },
        areActionsEnabled = !isEditingRestaurantName.value
    ) { innerPadding ->
        restaurantUIState.selectedRestaurant?.let { currentSelectedRestaurant ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Header that includes the restaurant's name, edit icon, and favorite icon
                RestaurantInfoScreenHeader(
                    editableHeader = {
                        // Editable header for the restaurant name
                        RestaurantNameEditableHeader(
                            isEditing = isEditingRestaurantName.value,
                            restaurantInput = restaurantViewModel.renameRestaurantInput,
                            onRestaurantNameChange = { newRestaurantName ->
                                restaurantViewModel.RestaurantContent().updateRestaurantRenameInput(newRestaurantName)
                            },
                            isInputInvalid = restaurantUIState.isRestaurantRenameInputInvalid,
                            onKeyboardDone = { restaurantViewModel.RestaurantContent().checkNewRestaurantInput() }
                        )
                    },
                    isEditing = isEditingRestaurantName.value,
                    restaurant = currentSelectedRestaurant,
                    isInputInvalid = restaurantUIState.isRestaurantRenameInputInvalid,
                    onToggleEditingRestaurantName = {
                        // If the restaurant's name is being edited, rename the restaurant
                        if (isEditingRestaurantName.value) {
                            restaurantViewModel.RestaurantContent().renameRestaurant(restaurantViewModel.renameRestaurantInput)
                        }

                        // Toggle the editing state
                        isEditingRestaurantName.value = !isEditingRestaurantName.value
                    },
                    onFavoriteClick = { restaurant ->
                        restaurantViewModel.RestaurantContent().toggleRestaurantIsFavorite(restaurant)
                    },
                )

                // Add order button
                Button(
                    onClick = {
                        restaurantViewModel.OrderContent().updateNewOrderInput("")
                        navController.navigate(AddOrder.route)
                    }
                ) { Text(text = stringResource(R.string.add_order)) }

                // Order list for the selected restaurant
                OrdersList(
                    ordersList = currentSelectedRestaurant.orders.reversed(),
                    onRemoveOrder = { orderToRemove ->
                        restaurantViewModel.OrderContent().removeOrder(orderToRemove)
                    }
                )
            }
        } ?: NoRestaurantList(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun RestaurantInfoScreenHeader(
    editableHeader: @Composable () -> Unit,
    isEditing: Boolean,
    isInputInvalid: Boolean,
    onToggleEditingRestaurantName: () -> Unit,
    restaurant: Restaurant,
    onFavoriteClick: (Restaurant) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        editableHeader()
        Row {
            if(isEditing) {
                AccessibleIcon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.submit_rename),
                    enabled = !isInputInvalid,
                    onClick = onToggleEditingRestaurantName
                )
            } else {
                AccessibleIcon(
                    imageVector = Icons.Default.Create,
                    contentDescription = stringResource(R.string.rename_restaurant),
                    onClick = onToggleEditingRestaurantName
                )
            }
            AccessibleIcon(
                imageVector = if(restaurant.isFavorite) {
                    Icons.Default.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                contentDescription = if(restaurant.isFavorite) {
                    stringResource(R.string.is_favorite)
                } else {
                    stringResource(R.string.is_not_favorite)
                },
                onClick = { onFavoriteClick(restaurant) }
            )
        }
    }
}

@Composable
fun RestaurantNameEditableHeader(
    isEditing: Boolean,
    restaurantInput: String,
    onRestaurantNameChange: (String) -> Unit,
    isInputInvalid: Boolean,
    onKeyboardDone: () -> Unit,
) {
    EditableHeader(
        isBeingEdited = isEditing,
        text = restaurantInput,
        onTextChange = onRestaurantNameChange,
        isInputInvalid = isInputInvalid,
        label = if (isInputInvalid) {
            if (restaurantInput.isBlank()) {
                stringResource(R.string.invalid_restaurant_name)
            } else {
                stringResource(R.string.restaurant_name_exists)
            }
        } else {
            stringResource(R.string.enter_new_restaurant_name)
        },
        onKeyboardDone = onKeyboardDone
    )
}

@Composable
fun OrdersList(
    ordersList: List<Order>,
    onRemoveOrder: (Order) -> Unit
) {
    LazyColumn {
        item {
            Text(
                text = stringResource(R.string.orders),
                style = Typography.headlineSmall
            )
        }
        itemsIndexed(
            items = ordersList
        ) { index, currentOrder ->
            // If it is the first order to list or its the first order of a new rating,
            // show a rating heading
            if(index == 0 || index > 0 && ordersList[index - 1].rating != currentOrder.rating) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.LightGray)
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        )
                        .height(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = pluralStringResource(R.plurals.stars, currentOrder.rating, currentOrder.rating),
                        style = Typography.titleLarge
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    // Display a row of stars for the rating
                    repeat(currentOrder.rating) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            OrderListItem(
                order = currentOrder,
                onRemoveOrder = onRemoveOrder
            )
        }
    }
}

@Composable
fun OrderListItem(
    order: Order,
    onRemoveOrder: (Order) -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        val isDeleteOrderDialogShowing = remember { mutableStateOf(false) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = order.name,
                style = Typography.titleLarge
            )
            AccessibleIcon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.delete_order),
            ) {
                isDeleteOrderDialogShowing.value = true
            }
        }
        Text(
            text = order.notes,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = Typography.bodyLarge
        )

        if(isDeleteOrderDialogShowing.value) {
            CustomAlertDialog(
                onDismiss = { isDeleteOrderDialogShowing.value = false },
                onConfirm = { onRemoveOrder(order) },
                title = stringResource(R.string.delete_order_title),
                body = stringResource(R.string.delete_order_body)
            )
        }
    }
}

@Preview
@Composable
fun RestaurantScreenPreview() {
    RestaurantInfoScreen()
}