package com.rhysstever.restaurantorders.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rhysstever.restaurantorders.R
import com.rhysstever.restaurantorders.ui.Restaurant
import com.rhysstever.restaurantorders.ui.RestaurantViewModel
import com.rhysstever.restaurantorders.ui.components.ButtonFill
import com.rhysstever.restaurantorders.ui.components.ScreenScaffold
import com.rhysstever.restaurantorders.ui.components.TopAppBarAction
import com.rhysstever.restaurantorders.ui.demoUIState
import com.rhysstever.restaurantorders.ui.navigation.AddRestaurant
import com.rhysstever.restaurantorders.ui.navigation.Home
import com.rhysstever.restaurantorders.ui.navigation.RestaurantInfo
import com.rhysstever.restaurantorders.ui.theme.AppIcon
import com.rhysstever.restaurantorders.ui.theme.AppIcons
import com.rhysstever.restaurantorders.ui.theme.Typography

@Composable
fun HomeScreen(
    navController: NavController,
    restaurantViewModel: RestaurantViewModel,
) {
    val uiState by restaurantViewModel.uiState.collectAsState()

    val toggleFavoriteAction = if (uiState.onlyShowFavorites) {
        TopAppBarAction(
            icon = AppIcons.FavoriteFilled,
            contentDescription = stringResource(R.string.top_app_bar_hide_favorites_button_cd),
            onClick = { restaurantViewModel.toggleShowingFavorites() }
        )
    } else {
        TopAppBarAction(
            icon = AppIcons.FavoriteOutline,
            contentDescription = stringResource(R.string.top_app_bar_show_favorites_button_cd),
            onClick = { restaurantViewModel.toggleShowingFavorites() }
        )
    }

    ScreenScaffold(
        currentScreen = Home,
        onBack = null,
        actions = listOf(
            toggleFavoriteAction,
            TopAppBarAction(
                icon = AppIcons.Add,
                contentDescription = stringResource(R.string.top_app_bar_add_restaurant_button_cd),
                onClick = { navController.navigate(AddRestaurant.route) },
            )
        )
    ) { innerPadding ->
        HomeScreenContent(
            restaurantsList = uiState.restaurants,
            showFavorites = uiState.onlyShowFavorites,
            onRestaurantClicked = { restaurant ->
                restaurantViewModel.RestaurantContent().selectRestaurant(restaurant)
                navController.navigate(RestaurantInfo.route)
            },
            onAddRestaurant = { navController.navigate(AddRestaurant.route) },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun HomeScreenContent(
    restaurantsList: List<Restaurant>,
    showFavorites: Boolean,
    onRestaurantClicked: (Restaurant) -> Unit,
    onAddRestaurant: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if(restaurantsList.isEmpty()) {
        ButtonFill(
            text = stringResource(R.string.get_started),
            onClick = onAddRestaurant,
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            leadingIcon = AppIcons.Add
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
        AppIcon(
            icon = if(restaurant.isFavorite) {
                AppIcons.FavoriteFilled
            } else {
                AppIcons.FavoriteOutline
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

@Preview
@Composable
private fun RestaurantHomeScreenWithListPreview() {
    HomeScreenContent(
        restaurantsList = demoUIState.restaurants,
        showFavorites = false,
        onRestaurantClicked = {},
        onAddRestaurant = {}
    )
}

@Preview
@Composable
private fun RestaurantHomeScreenNoListPreview() {
    HomeScreenContent(
        restaurantsList = demoUIState.copy(restaurants = emptyList()).restaurants,
        showFavorites = false,
        onRestaurantClicked = {},
        onAddRestaurant = {}
    )
}