package com.rhysstever.restaurantorders.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.rhysstever.restaurantorders.RestaurantDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantTopAppBar(
    currentScreen: RestaurantDestination,
    onBack: (() -> Unit)? = null,
    onAdd: (() -> Unit)? = null,
    onlyShowFavorites: (Pair<Boolean, () -> Unit>)? = null,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = currentScreen.route)
        },
        navigationIcon = {
            onBack?.let {
                AccessibleIcon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    onClick = onBack
                )
            }
        },
        actions = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                onlyShowFavorites?.let {
                    AccessibleIcon(
                        imageVector = if(it.first) {
                            Icons.Default.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },
                        contentDescription = "Show Favorites",
                        onClick = it.second
                    )
                }
                onAdd?.let {
                    AccessibleIcon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Restaurant",
                        onClick = onAdd
                    )
                }
            }
        }
    )
}