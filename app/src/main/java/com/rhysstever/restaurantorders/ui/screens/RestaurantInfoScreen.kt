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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rhysstever.restaurantorders.R
import com.rhysstever.restaurantorders.ui.Restaurant
import com.rhysstever.restaurantorders.ui.RestaurantUIState
import com.rhysstever.restaurantorders.ui.RestaurantViewModel
import com.rhysstever.restaurantorders.ui.Visit
import com.rhysstever.restaurantorders.ui.components.AccessibleIcon
import com.rhysstever.restaurantorders.ui.components.ButtonFill
import com.rhysstever.restaurantorders.ui.components.CustomAlertDialog
import com.rhysstever.restaurantorders.ui.components.EditableText
import com.rhysstever.restaurantorders.ui.components.ScreenScaffold
import com.rhysstever.restaurantorders.ui.components.TopAppBarAction
import com.rhysstever.restaurantorders.ui.components.displayDate
import com.rhysstever.restaurantorders.ui.demoUIStateSelected
import com.rhysstever.restaurantorders.ui.navigation.AddVisit
import com.rhysstever.restaurantorders.ui.navigation.Home
import com.rhysstever.restaurantorders.ui.navigation.RestaurantInfo
import com.rhysstever.restaurantorders.ui.navigation.VisitInfo
import com.rhysstever.restaurantorders.ui.theme.AppIcon
import com.rhysstever.restaurantorders.ui.theme.AppIcons
import com.rhysstever.restaurantorders.ui.theme.Typography

@Composable
fun RestaurantInfoScreen(
    navController: NavController,
    restaurantViewModel: RestaurantViewModel
) {
    val uiState by restaurantViewModel.uiState.collectAsState()

    ScreenScaffold(
        currentScreen = RestaurantInfo,
        onBack = {
            restaurantViewModel.RestaurantContent().selectRestaurant(null)
            navController.navigate(Home.route)
        },
        actions = restaurantInfoScreenActionsList(
            uiState = uiState,
            onAdd = { navController.navigate(AddVisit.route) },
            onToggleFavorite = { restaurant ->
                restaurantViewModel.RestaurantContent().toggleRestaurantIsFavorite(restaurant)
            }
        )
    ) { innerPadding ->
        RestaurantInfoScreenContent(
            selectedRestaurant = uiState.selectedRestaurant,
            onRenameRestaurant = { restaurantName ->
                restaurantViewModel.RestaurantContent().renameRestaurant(restaurantName)
            },
            onCheckRestaurantRenameInput = { newRestaurantName ->
                restaurantViewModel.RestaurantContent().checkRestaurantRenameInput(newRestaurantName)
            },
            isRestaurantRenameInputInvalid = uiState.isRestaurantRenameInputInvalid,
            onVisitSelected = { selectedVisit ->
                restaurantViewModel.VisitContent().selectVisit(selectedVisit)
                navController.navigate(VisitInfo.route)
            },
            onRemoveVisit = { visitToRemove ->
                restaurantViewModel.VisitContent().removeVisit(visitToRemove)
            },
            onCheckNewRestaurantInput = { restaurantName ->
                restaurantViewModel.RestaurantContent().checkNewRestaurantInput(restaurantName)
            },
            onAddVisit = { navController.navigate(AddVisit.route) },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun RestaurantInfoScreenContent(
    selectedRestaurant: Restaurant?,
    onRenameRestaurant: (String) -> Unit,
    onCheckRestaurantRenameInput: (String) -> Unit,
    isRestaurantRenameInputInvalid: Boolean,
    onCheckNewRestaurantInput: (String) -> Unit,
    onVisitSelected: (Visit) -> Unit,
    onRemoveVisit: (Visit) -> Unit,
    onAddVisit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    selectedRestaurant?.let {
        val isEditingRestaurantName = remember { mutableStateOf(false) }
        val (restaurantName, onRestaurantNameChange) = remember { mutableStateOf(selectedRestaurant.name) }

        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                EditableText(
                    isBeingEdited = isEditingRestaurantName.value,
                    text = restaurantName,
                    onTextChange = { newRestaurantName ->
                        onRestaurantNameChange(newRestaurantName)
                        onCheckRestaurantRenameInput(newRestaurantName)
                    },
                    isInputInvalid = isRestaurantRenameInputInvalid,
                    label = if (isRestaurantRenameInputInvalid) {
                        if (restaurantName.isBlank()) {
                            stringResource(R.string.invalid_restaurant_name)
                        } else {
                            stringResource(R.string.restaurant_name_exists)
                        }
                    } else {
                        stringResource(R.string.enter_new_restaurant_name)
                    },
                    onKeyboardDone = { onCheckNewRestaurantInput(restaurantName) },
                    modifier = Modifier.weight(1f)
                )
                val onToggleEditingRestaurantName = {
                    // If the restaurant's name is being edited, rename the restaurant
                    if (isEditingRestaurantName.value) {
                        onRestaurantNameChange(restaurantName)
                        onRenameRestaurant(restaurantName)
                    }
                    // Toggle the editing state
                    isEditingRestaurantName.value = !isEditingRestaurantName.value
                }

                if(isEditingRestaurantName.value) {
                    AccessibleIcon(
                        icon = AppIcons.Check,
                        contentDescription = stringResource(R.string.submit_rename),
                        enabled = !isRestaurantRenameInputInvalid,
                        onClick = onToggleEditingRestaurantName
                    )
                } else {
                    AccessibleIcon(
                        icon = AppIcons.EditOutline,
                        contentDescription = stringResource(R.string.rename_restaurant),
                        onClick = onToggleEditingRestaurantName
                    )
                }
            }

            if(selectedRestaurant.visits.isNotEmpty()) {
                VisitsList(
                    visits = selectedRestaurant.visits.reversed(),
                    onVisitSelected = onVisitSelected,
                    onRemoveVisit = onRemoveVisit,
                )
            } else {
                ButtonFill(
                    text = stringResource(R.string.add_a_visit),
                    onClick = onAddVisit,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    leadingIcon = AppIcons.Add
                )
            }
        }
    } ?: NoRestaurantSelectedInfo(modifier = modifier)
}

@Composable
fun NoRestaurantSelectedInfo(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .semantics(mergeDescendants = true) {},
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.no_restaurant_text),
            textAlign = TextAlign.Center,
            style = Typography.bodyLarge
        )
    }
}

@Composable
private fun VisitsList(
    visits: List<Visit>,
    onVisitSelected: (Visit) -> Unit,
    onRemoveVisit: (Visit) -> Unit
) {
    LazyColumn {
        item {
            Text(
                text = stringResource(R.string.visits),
                style = Typography.headlineSmall
            )
        }
        items(
            count = visits.size
        ) { currentVisit ->
            VisitListItem(
                visit = visits[currentVisit],
                onVisitSelected = onVisitSelected,
                onRemoveVisit = onRemoveVisit
            )
        }
    }
}

@Composable
private fun VisitListItem(
    visit: Visit,
    onVisitSelected: (Visit) -> Unit,
    onRemoveVisit: (Visit) -> Unit,
) {
    val isDeleteVisitDialogShowing = remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .semantics(mergeDescendants = true) {},
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = visit.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp),
                style = Typography.titleLarge
            )
            Row {
                AccessibleIcon(
                    icon = AppIcons.InfoOutline,
                    contentDescription = stringResource(R.string.visit_info),
                ) {
                    onVisitSelected(visit)
                }
                AccessibleIcon(
                    icon = AppIcons.Close,
                    contentDescription = stringResource(R.string.delete_visit),
                ) {
                    isDeleteVisitDialogShowing.value = true
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            visit.rating?.let { rating ->
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
            visit.dateVisited?.let {
                Text(
                    text = "${stringResource(R.string.date_visited)}: ${displayDate((it))}",
                    style = Typography.bodyLarge
                )
            }
        }

        Text(
            text = visit.notes,
            style = Typography.bodyLarge
        )

        if(isDeleteVisitDialogShowing.value) {
            CustomAlertDialog(
                onDismiss = { isDeleteVisitDialogShowing.value = false },
                onConfirm = {
                    onRemoveVisit(visit)
                    isDeleteVisitDialogShowing.value = false
                },
                title = stringResource(R.string.delete_visit_title),
                body = stringResource(R.string.delete_visit_body)
            )
        }
    }
}

@Composable
private fun restaurantInfoScreenActionsList(
    uiState: RestaurantUIState,
    onAdd: () -> Unit,
    onToggleFavorite: (Restaurant) -> Unit,
): List<TopAppBarAction> {
    val list = mutableListOf<TopAppBarAction>()

    val favoriteAction = uiState.selectedRestaurant?.let {
        if(it.isFavorite) {
            TopAppBarAction(
                icon = AppIcons.FavoriteFilled,
                contentDescription = stringResource(R.string.top_app_bar_unfavorite_button_cd),
                onClick = { onToggleFavorite(it) }
            )
        } else {
            TopAppBarAction(
                icon = AppIcons.FavoriteOutline,
                contentDescription = stringResource(R.string.top_app_bar_favorite_button_cd),
                onClick = { onToggleFavorite(it) }
            )
        }
    }
    favoriteAction?.let { list.add(it) }

    list.add(
        TopAppBarAction(
            icon = AppIcons.Add,
            contentDescription = stringResource(R.string.top_app_bar_add_visit_button_cd),
            onClick = onAdd
        )
    )

    return list.toList()
}

@Preview
@Composable
private fun RestaurantInfoScreenPreview() {
    RestaurantInfoScreenContent(
        selectedRestaurant = demoUIStateSelected.selectedRestaurant,
        onRenameRestaurant = {},
        onCheckRestaurantRenameInput = {},
        isRestaurantRenameInputInvalid = false,
        onCheckNewRestaurantInput = {},
        onVisitSelected = {},
        onRemoveVisit = {},
        onAddVisit = {},
    )
}

@Preview
@Composable
private fun RestaurantInfoScreenNoSelectionPreview() {
    RestaurantInfoScreenContent(
        selectedRestaurant = null,
        onRenameRestaurant = {},
        onCheckRestaurantRenameInput = {},
        isRestaurantRenameInputInvalid = false,
        onCheckNewRestaurantInput = {},
        onVisitSelected = {},
        onRemoveVisit = {},
        onAddVisit = {},
    )
}