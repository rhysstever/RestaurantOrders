package com.rhysstever.restaurantorders.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rhysstever.restaurantorders.Home
import com.rhysstever.restaurantorders.R
import com.rhysstever.restaurantorders.ui.Restaurant
import com.rhysstever.restaurantorders.ui.RestaurantUIState
import com.rhysstever.restaurantorders.ui.components.ScreenScaffold
import com.rhysstever.restaurantorders.ui.demoUIState
import com.rhysstever.restaurantorders.ui.theme.Typography

@Composable
fun HomeScreen(
    state: RestaurantUIState,
    onAdd: () -> Unit,
    onShowFavorites: () -> Unit,
    onRestaurantClicked: (Restaurant) -> Unit,
) {
    ScreenScaffold(
        currentScreen = Home,
        onBack = null,
        onAdd = onAdd,
        showFavorites = Pair(
            state.onlyShowFavorites,
            onShowFavorites
        )
    ) { innerPadding ->
        HomeScreenContent(
            restaurantsList = state.restaurants,
            showFavorites = state.onlyShowFavorites,
            onRestaurantClicked = onRestaurantClicked,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun HomeScreenContent(
    restaurantsList: List<Restaurant>,
    showFavorites: Boolean,
    onRestaurantClicked: (Restaurant) -> Unit,
    modifier: Modifier = Modifier,
) {
    if(restaurantsList.isEmpty()) {
        NoRestaurantList(
            modifier = modifier
        )
    } else {
        // If there are restaurants to list, show the list
        // Filter the list down to only favorites if the toggle is on
        RestaurantList(
            restaurantList = if(showFavorites) {
                restaurantsList.filter { it.isFavorite }
            } else {
                restaurantsList
            },
            onRestaurantClicked = onRestaurantClicked,
            modifier = modifier
        )
    }
}

@Composable
private fun RestaurantList(
    restaurantList: List<Restaurant>,
    onRestaurantClicked: (Restaurant) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        restaurantList.forEach { restaurant ->
            item {
                RestaurantListItem(
                    restaurant = restaurant,
                    onRestaurantClicked = onRestaurantClicked
                )
            }
        }
    }
}

@Composable
private fun RestaurantListItem(
    restaurant: Restaurant,
    onRestaurantClicked: (Restaurant) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button) {
                onRestaurantClicked(restaurant)
            }
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
            .semantics(mergeDescendants = true) {},
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
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
            modifier = Modifier.requiredSizeIn(
                minWidth = 24.dp,
                minHeight = 24.dp
            )
        )
        Text(
            text = restaurant.name,
            style = Typography.headlineSmall,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun NoRestaurantList(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.get_started),
            style = Typography.bodyLarge
        )
    }
}

@Preview
@Composable
private fun RestaurantHomeScreenWithListPreview() {
    HomeScreen(
        state = demoUIState,
        onAdd = {},
        onShowFavorites = {},
        onRestaurantClicked = {}
    )
}

@Preview
@Composable
private fun RestaurantHomeScreenNoListPreview() {
    HomeScreen(
        state = demoUIState.copy(restaurants = emptyList()),
        onAdd = {},
        onShowFavorites = {},
        onRestaurantClicked = {}
    )
}