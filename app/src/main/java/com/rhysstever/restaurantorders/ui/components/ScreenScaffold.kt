package com.rhysstever.restaurantorders.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rhysstever.restaurantorders.RestaurantDestination

@Composable
fun ScreenScaffold(
    currentScreen: RestaurantDestination,
    onBack: (() -> Unit)?,
    onAdd: (() -> Unit)?,
    areActionsEnabled: Boolean = true,
    showFavorites: Pair<Boolean, () -> Unit>? = null,
    contentToShow: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RestaurantTopAppBar(
                currentScreen = currentScreen,
                onBack = onBack,
                onAdd = onAdd,
                areActionsEnabled = areActionsEnabled,
                onlyShowFavorites = showFavorites
            )
        }
    ) { innerPadding ->
        contentToShow(innerPadding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantTopAppBar(
    currentScreen: RestaurantDestination,
    onBack: (() -> Unit)? = null,
    onAdd: (() -> Unit)? = null,
    areActionsEnabled: Boolean = true,
    onlyShowFavorites: Pair<Boolean, () -> Unit>? = null,
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
                    enabled = areActionsEnabled,
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
                        enabled = areActionsEnabled,
                        onClick = it.second
                    )
                }
                onAdd?.let {
                    AccessibleIcon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Restaurant",
                        enabled = areActionsEnabled,
                        onClick = onAdd
                    )
                }
            }
        }
    )
}