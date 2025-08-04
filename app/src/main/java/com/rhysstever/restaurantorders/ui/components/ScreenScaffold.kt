package com.rhysstever.restaurantorders.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rhysstever.restaurantorders.R
import com.rhysstever.restaurantorders.ui.navigation.RestaurantDestination
import com.rhysstever.restaurantorders.ui.theme.AppIcons

@Composable
fun ScreenScaffold(
    currentScreen: RestaurantDestination,
    onBack: (() -> Unit)?,
    actions: List<TopAppBarAction>? = null,
    contentToShow: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RestaurantTopAppBar(
                currentScreen = currentScreen,
                onBack = onBack,
                actions = actions,
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
    actions: List<TopAppBarAction>? = null,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = currentScreen.route)
        },
        navigationIcon = {
            onBack?.let {
                AccessibleIcon(
                    icon = AppIcons.ChevronLeft,
                    contentDescription = stringResource(id = R.string.top_app_bar_back_button_cd),
                    onClick = onBack
                )
            }
        },
        actions = {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                actions?.forEach { action ->
                    AccessibleIcon(
                        icon = action.icon,
                        contentDescription = action.contentDescription,
                        onClick = action.onClick,
                        enabled = action.enabled,
                    )
                }
            }
        }
    )
}

data class TopAppBarAction(
    val icon: AppIcons,
    val contentDescription: String,
    val onClick: () -> Unit,
    val enabled: Boolean = true
)