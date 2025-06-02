package com.rhysstever.restaurantorders.ui.screens

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

                if(currentSelectedRestaurant.orders.isNotEmpty()) {
                    // Order list for the selected restaurant
                    OrdersList(
                        ordersList = currentSelectedRestaurant.orders.reversed(),
                        onRemoveOrder = { orderToRemove ->
                            restaurantViewModel.OrderContent().removeOrder(orderToRemove)
                        }
                    )
                } else {
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            order.rating?.let {
                StarsRow(it)
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

@Composable
fun StarsRow(numberOfStars: Int) {
    Row(
        modifier = Modifier.height(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(numberOfStars) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview
@Composable
fun RestaurantScreenPreview() {
    RestaurantInfoScreen()
}