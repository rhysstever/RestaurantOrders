package com.rhysstever.restaurantorders.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rhysstever.restaurantorders.RestaurantDestination
import com.rhysstever.restaurantorders.restaurantTabRowScreens
import com.rhysstever.restaurantorders.ui.Restaurant

@Composable
fun RestaurantBottomTabRow(
    onTabSelected: (RestaurantDestination) -> Unit,
    currentScreen: RestaurantDestination
) {
    RestaurantBottomTabRow(
        allDestinations = restaurantTabRowScreens,
        onTabSelected = onTabSelected,
        currentScreen = currentScreen
    )
}

@Composable
fun RestaurantBottomTabRow(
    allDestinations: List<RestaurantDestination>,
    onTabSelected: (RestaurantDestination) -> Unit,
    currentScreen: RestaurantDestination
) {
    Surface(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.selectableGroup(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            allDestinations.forEach { screen ->
                RestaurantTab(
                    screen = screen,
                    onSelected = { onTabSelected(screen) },
                    selected = screen == currentScreen
                )
            }
        }
    }
}

@Composable
fun RestaurantTab(
    screen: RestaurantDestination,
    onSelected: () -> Unit,
    selected: Boolean
) {
    Column(
        modifier = Modifier
            .selectable(
                selected = selected,
                onClick = onSelected,
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple()
            )
            .sizeIn(
                minWidth = 48.dp,
                minHeight = 48.dp
            )
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Icon(
//            imageVector = if (selected) {
//                screen.selectedIcon
//            } else {
//                screen.unselectedIcon
//            },
//            contentDescription = screen.route,
//            modifier = Modifier.sizeIn(
//                minWidth = 24.dp,
//                minHeight = 24.dp
//            )
//        )
        Text(
            text = screen.route,
            textAlign = TextAlign.Center
        )
    }
}