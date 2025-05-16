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

    ScreenScaffold(
        currentScreen = RestaurantInfo,
        onBack = {
            restaurantViewModel.RestaurantContent().updateSelectedRestaurant(null)
            navController.navigate(Home.route)
        },
        onAdd = {
            restaurantViewModel.OrderContent().updateNewOrderInput("")
            navController.navigate(AddOrder.route)
        },
    ) { innerPadding ->
        restaurantUIState.selectedRestaurant?.let {
            RestaurantScreenContent(
                restaurant = it,
                restaurantNameTitle = {
                    EditableRestaurantTitle(
                        isBeingEdited = isEditingRestaurantName.value,
                        restaurantTitle = restaurantViewModel.newRestaurantInput,
                        onRestaurantNameChange = { newRestaurantName ->
                            restaurantViewModel.RestaurantContent().updateNewRestaurantInput(newRestaurantName)
                        },
                        isInputInvalid = restaurantUIState.isNewRestaurantInputInvalid,
                        onKeyboardDone = { restaurantViewModel.RestaurantContent().checkNewRestaurantInput() },
                    )
                },
                isEditingRestaurantName = isEditingRestaurantName.value,
                isEditingRestaurantNameInvalid = if(isEditingRestaurantName.value) {
                    restaurantUIState.isNewRestaurantInputInvalid ?: false
                } else true,
                onToggleEditingRestaurantName = {
                    if (isEditingRestaurantName.value) {
                        restaurantViewModel.RestaurantContent().renameRestaurant(restaurantViewModel.newRestaurantInput)
                    }

                    isEditingRestaurantName.value = !isEditingRestaurantName.value
                },
                onFavoriteClick = { restaurant ->
                    restaurantViewModel.RestaurantContent().toggleRestaurantIsFavorite(restaurant)
                },
                onAddNewOrder = {
                    restaurantViewModel.OrderContent().updateNewOrderInput("")
                    navController.navigate(AddOrder.route)
                },
                modifier = Modifier.padding(innerPadding)
            )
        } ?: NoRestaurantList(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun RestaurantScreenContent(
    restaurant: Restaurant,
    restaurantNameTitle: @Composable () -> Unit,
    isEditingRestaurantName: Boolean,
    isEditingRestaurantNameInvalid: Boolean,
    onToggleEditingRestaurantName: () -> Unit,
    onFavoriteClick: (Restaurant) -> Unit,
    onAddNewOrder: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            restaurantNameTitle()
            Row {
                AccessibleIcon(
                    imageVector = if(isEditingRestaurantName) {
                        Icons.Default.Check
                    } else {
                        Icons.Default.Create
                    },
                    contentDescription = stringResource(R.string.edit_restaurant_name),
                    enabled = !(isEditingRestaurantName && isEditingRestaurantNameInvalid),
                    onClick = onToggleEditingRestaurantName
                )
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

        Button(
            onClick = onAddNewOrder
        ) { Text(text = stringResource(R.string.add_order)) }

        LazyColumn {
            item {
                Text(
                    text = stringResource(R.string.orders),
                    style = Typography.headlineSmall
                )
            }
            itemsIndexed(
                items = restaurant.orders.reversed()
            ) { index, order ->
                // If it is the first order to list or its the first order of a new rating,
                // show a rating heading
                if(index == 0 || index > 0 && restaurant.orders.reversed()[index - 1].rating != order.rating) {
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
                            text = pluralStringResource(R.plurals.stars, order.rating),
                            style = Typography.titleLarge
                        )
                        Spacer(modifier = modifier.width(4.dp))
                        // Display a row of stars for the rating
                        repeat(order.rating) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                OrderListItem(order = order)
            }
        }
    }
}

@Composable
fun EditableRestaurantTitle(
    isBeingEdited: Boolean,
    restaurantTitle: String,
    onRestaurantNameChange: (String) -> Unit,
    isInputInvalid: Boolean?,
    onKeyboardDone: () -> Unit,
) {
    EditableHeader(
        isBeingEdited = isBeingEdited,
        text = restaurantTitle,
        onTextChange = onRestaurantNameChange,
        isInputInvalid = isInputInvalid,
        label = {
            isInputInvalid?.let { isInvalid ->
                if (isInvalid) {
                    if (restaurantTitle.isBlank()) {
                        Text(text = stringResource(R.string.invalid_restaurant_name))
                    } else {
                        Text(text = stringResource(R.string.restaurant_name_exists))
                    }
                } else {
                    Text(text = stringResource(R.string.enter_restaurant_name))
                }
            } ?: Text(text = stringResource(R.string.enter_restaurant_name))
        },
        onKeyboardDone = onKeyboardDone
    )
}

@Composable
fun OrderListItem(order: Order) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = order.name,
            style = Typography.titleLarge
        )
        Text(
            text = order.notes,
            style = Typography.bodyLarge
        )
    }
}

@Preview
@Composable
fun RestaurantScreenPreview() {
    RestaurantInfoScreen()
}