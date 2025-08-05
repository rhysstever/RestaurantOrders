package com.rhysstever.restaurantorders.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rhysstever.restaurantorders.R
import com.rhysstever.restaurantorders.ui.Order
import com.rhysstever.restaurantorders.ui.RestaurantViewModel
import com.rhysstever.restaurantorders.ui.Visit
import com.rhysstever.restaurantorders.ui.components.AccessibleIcon
import com.rhysstever.restaurantorders.ui.components.ButtonFill
import com.rhysstever.restaurantorders.ui.components.CustomAlertDialog
import com.rhysstever.restaurantorders.ui.components.ScreenScaffold
import com.rhysstever.restaurantorders.ui.components.TopAppBarAction
import com.rhysstever.restaurantorders.ui.components.displayDate
import com.rhysstever.restaurantorders.ui.demoUIStateSelected
import com.rhysstever.restaurantorders.ui.navigation.AddOrder
import com.rhysstever.restaurantorders.ui.navigation.AddVisit
import com.rhysstever.restaurantorders.ui.navigation.RestaurantInfo
import com.rhysstever.restaurantorders.ui.navigation.VisitInfo
import com.rhysstever.restaurantorders.ui.theme.AppIcon
import com.rhysstever.restaurantorders.ui.theme.AppIcons
import com.rhysstever.restaurantorders.ui.theme.Typography

@Composable
fun VisitInfoScreen(
    navController: NavController,
    restaurantViewModel: RestaurantViewModel
) {
    val uiState by restaurantViewModel.uiState.collectAsState()

    ScreenScaffold(
        currentScreen = VisitInfo,
        onBack = { navController.navigate(RestaurantInfo.route) },
        actions = uiState.selectedRestaurant?.let {
            listOf(
                TopAppBarAction(
                    icon = AppIcons.Add,
                    contentDescription = stringResource(R.string.top_app_bar_add_order_button_cd),
                    onClick = { navController.navigate(AddOrder.route) }
                )
            )
        },
    ) { innerPadding ->
        uiState.selectedVisit?.let { currentlySelectedVisit ->
            VisitInfoScreenContent(
                visit = currentlySelectedVisit,
                onEditVisit = {
                    navController.navigate(AddVisit.route)
                },
                onRemoveOrder = { orderToRemove ->
                    restaurantViewModel.OrderContent().removeOrder(orderToRemove)
                },
                onAddOrder = { navController.navigate(AddOrder.route) },
                modifier = Modifier.padding(innerPadding)
            )
        } ?: NoRestaurantSelectedInfo(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
private fun VisitInfoScreenContent(
    visit: Visit,
    onEditVisit: () -> Unit,
    onRemoveOrder: (Order) -> Unit,
    onAddOrder: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = visit.name,
                modifier = Modifier.weight(1f),
                style = Typography.headlineLarge,
            )
            AccessibleIcon(
                icon = AppIcons.EditOutline,
                contentDescription = stringResource(R.string.edit_visit),
                onClick = onEditVisit
            )
        }

        visit.rating?.let { rating ->
            Row(
                modifier = Modifier.semantics(true) {},
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${stringResource(R.string.overall_rating)}: ",
                    style = Typography.bodyLarge
                )
                repeat(rating) { index ->
                    AppIcon(
                        icon = AppIcons.StarFilled,
                        contentDescription = if(index == rating - 1) {
                            pluralStringResource(R.plurals.stars, index + 1, index + 1)
                        } else {
                            null
                        },
                        modifier = Modifier.requiredSize(16.dp)
                    )
                }
            }
        }
        visit.dateVisited?.let {
            Text(
                text = "${stringResource(R.string.date_visited)}: ${displayDate((it))}",
                style = Typography.bodyLarge
            )
        }
        Text(
            text = visit.notes,
            style = Typography.bodyLarge
        )

        if(visit.orders.isNotEmpty()) {
            OrdersList(
                orders = visit.orders.reversed(),
                onRemoveOrder = onRemoveOrder
            )
        } else {
            ButtonFill(
                text = stringResource(R.string.add_an_order),
                onClick = onAddOrder,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                leadingIcon = AppIcons.Add
            )
        }
    }
}

@Composable
private fun OrdersList(
    orders: List<Order>,
    onRemoveOrder: (Order) -> Unit
) {
    LazyColumn {
        item {
            Text(
                text = stringResource(R.string.orders),
                modifier = Modifier.padding(top = 4.dp),
                style = Typography.headlineSmall
            )
        }
        items(
            count = orders.size
        ) { currentOrder ->
            OrderListItem(
                order = orders[currentOrder],
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
            .padding(vertical = 8.dp)
            .semantics(mergeDescendants = true) {},
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        val isDeleteOrderDialogShowing = remember { mutableStateOf(false) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = order.name,
                modifier = Modifier.weight(1f),
                style = Typography.titleLarge
            )
            AccessibleIcon(
                icon = AppIcons.Close,
                contentDescription = stringResource(R.string.delete_order),
            ) {
                isDeleteOrderDialogShowing.value = true
            }
        }
        order.rating?.let { rating ->
            Row(
                modifier = Modifier.height(24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(rating) { index ->
                    AppIcon(
                        icon = AppIcons.StarFilled,
                        contentDescription = if(index == rating - 1) {
                            pluralStringResource(R.plurals.stars, index + 1, index + 1)
                        } else {
                            null
                        },
                        modifier = Modifier.requiredSize(16.dp)
                    )
                }
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

@Preview
@Composable
private fun VisitScreenPreview() {
    VisitInfoScreenContent(
        visit = demoUIStateSelected.selectedVisit!!,
        onEditVisit = {},
        onRemoveOrder = {},
        onAddOrder = {}
    )
}