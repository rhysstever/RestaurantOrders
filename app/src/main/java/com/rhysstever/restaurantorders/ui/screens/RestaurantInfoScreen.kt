package com.rhysstever.restaurantorders.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rhysstever.restaurantorders.AddOrder
import com.rhysstever.restaurantorders.Home
import com.rhysstever.restaurantorders.R
import com.rhysstever.restaurantorders.RestaurantInfo
import com.rhysstever.restaurantorders.ui.Order
import com.rhysstever.restaurantorders.ui.RestaurantViewModel
import com.rhysstever.restaurantorders.ui.components.AccessibleIcon
import com.rhysstever.restaurantorders.ui.components.CustomAlertDialog
import com.rhysstever.restaurantorders.ui.components.EditableText
import com.rhysstever.restaurantorders.ui.components.ScreenScaffold
import com.rhysstever.restaurantorders.ui.components.displayDate
import com.rhysstever.restaurantorders.ui.theme.Typography

@Composable
fun RestaurantInfoScreen(
    navController: NavHostController = rememberNavController(),
    restaurantViewModel: RestaurantViewModel = viewModel()
) {
    val restaurantUIState by restaurantViewModel.uiState.collectAsState()
    val isEditingRestaurantName = remember { mutableStateOf(false) }

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
                // Screen title that includes the restaurant's name, edit icon, and favorite icon
                RestaurantInfoScreenTitle(
                    isEditing = isEditingRestaurantName.value,
                    onToggleEditingRestaurantName = {
                        // If the restaurant's name is being edited, rename the restaurant
                        if (isEditingRestaurantName.value) {
                            restaurantViewModel.RestaurantContent().renameRestaurant(restaurantViewModel.renameRestaurantInput)
                        }
                        // Toggle the editing state
                        isEditingRestaurantName.value = !isEditingRestaurantName.value
                    },
                    restaurantInput = restaurantViewModel.renameRestaurantInput,
                    onRestaurantNameChange = { newRestaurantName ->
                        restaurantViewModel.RestaurantContent().updateRestaurantRenameInput(newRestaurantName)
                    },
                    isInputInvalid = restaurantUIState.isRestaurantRenameInputInvalid,
                    isRestaurantFavorite = currentSelectedRestaurant.isFavorite,
                    onFavoriteClick = {
                        restaurantViewModel.RestaurantContent().toggleRestaurantIsFavorite(currentSelectedRestaurant)
                    },
                    onKeyboardDone = { restaurantViewModel.RestaurantContent().checkNewRestaurantInput() }
                )

                if(currentSelectedRestaurant.orders.isNotEmpty()) {
                    // Order list for the selected restaurant
                    OrdersList(
                        ordersList = currentSelectedRestaurant.orders.reversed(),
                        onRemoveOrder = { orderToRemove ->
                            restaurantViewModel.OrderContent().removeOrder(orderToRemove)
                        }
                    )
                } else {
                    NoOrdersList()
                }
            }
        } ?: NoRestaurantList(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun RestaurantInfoScreenTitle(
    isEditing: Boolean,
    onToggleEditingRestaurantName: () -> Unit,
    restaurantInput: String,
    onRestaurantNameChange: (String) -> Unit,
    isInputInvalid: Boolean,
    isRestaurantFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onKeyboardDone: () -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        EditableText(
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
                imageVector = if(isRestaurantFavorite) {
                    Icons.Default.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                contentDescription = if(isRestaurantFavorite) {
                    stringResource(R.string.is_favorite)
                } else {
                    stringResource(R.string.is_not_favorite)
                },
                onClick = onFavoriteClick
            )
        }
    }
}

@Composable
private fun NoOrdersList() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.add_an_order),
            style = Typography.bodyLarge
        )
    }
}

@Composable
private fun OrdersList(
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
        items(
            count = ordersList.size
        ) { currentOrder ->
            OrderListItem(
                order = ordersList[currentOrder],
                onRemoveOrder = onRemoveOrder
            )
        }
    }
}

@Composable
private fun OrderListItem(
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            order.rating?.let {
                Row(
                    modifier = Modifier.height(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(it) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.requiredSize(16.dp)
                        )
                    }
                }
            }
            order.dateOrdered?.let {
                Text(
                    text = "${stringResource(R.string.date_ordered)}: ${displayDate(it)}",
                    style = Typography.bodyLarge
                )
            }
        }

        Text(
            text = order.notes,
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