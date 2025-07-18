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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rhysstever.restaurantorders.R
import com.rhysstever.restaurantorders.VisitInfo
import com.rhysstever.restaurantorders.ui.Order
import com.rhysstever.restaurantorders.ui.RestaurantUIState
import com.rhysstever.restaurantorders.ui.Visit
import com.rhysstever.restaurantorders.ui.components.AccessibleIcon
import com.rhysstever.restaurantorders.ui.components.CustomAlertDialog
import com.rhysstever.restaurantorders.ui.components.ScreenScaffold
import com.rhysstever.restaurantorders.ui.components.displayDate
import com.rhysstever.restaurantorders.ui.demoUIStateSelected
import com.rhysstever.restaurantorders.ui.theme.Typography

@Composable
fun VisitInfoScreen(
    state: RestaurantUIState,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onEditVisit: () -> Unit,
    onRemoveOrder: (Order) -> Unit
) {
    ScreenScaffold(
        currentScreen = VisitInfo,
        onBack = onBack,
        onAdd = state.selectedRestaurant?.let { onAdd },
    ) { innerPadding ->
        state.selectedVisit?.let { currentlySelectedVisit ->
            VisitInfoScreenContent(
                visit = currentlySelectedVisit,
                onEditVisit = onEditVisit,
                onRemoveOrder = onRemoveOrder,
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
    modifier: Modifier = Modifier,
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
            Text(
                text = visit.name,
                style = Typography.headlineLarge,
            )
            AccessibleIcon(
                imageVector = Icons.Default.Create,
                contentDescription = stringResource(R.string.edit_visit),
                onClick = onEditVisit
            )
        }

        visit.rating?.let {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${stringResource(R.string.overall_rating)}: ",
                    style = Typography.bodyLarge
                )
                repeat(it) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "$it ${pluralStringResource(R.plurals.stars, it, it)}",
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
            NoOrdersList()
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
                            contentDescription = "$it ${pluralStringResource(R.plurals.stars, it, it)}",
                            modifier = Modifier.requiredSize(16.dp)
                        )
                    }
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
fun VisitScreenPreview() {
    VisitInfoScreen(
        state = demoUIStateSelected,
        onBack = {},
        onAdd = {},
        onEditVisit = {},
        onRemoveOrder = {}
    )
}