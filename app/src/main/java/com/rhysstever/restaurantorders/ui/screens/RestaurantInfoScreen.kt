package com.rhysstever.restaurantorders.ui.screens

import android.util.Log
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Info
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rhysstever.restaurantorders.R
import com.rhysstever.restaurantorders.RestaurantInfo
import com.rhysstever.restaurantorders.ui.Restaurant
import com.rhysstever.restaurantorders.ui.RestaurantUIState
import com.rhysstever.restaurantorders.ui.Visit
import com.rhysstever.restaurantorders.ui.components.AccessibleIcon
import com.rhysstever.restaurantorders.ui.components.CustomAlertDialog
import com.rhysstever.restaurantorders.ui.components.EditableText
import com.rhysstever.restaurantorders.ui.components.ScreenScaffold
import com.rhysstever.restaurantorders.ui.components.displayDate
import com.rhysstever.restaurantorders.ui.demoUIState
import com.rhysstever.restaurantorders.ui.demoUIStateSelected
import com.rhysstever.restaurantorders.ui.theme.Typography

@Composable
fun RestaurantInfoScreen(
    state: RestaurantUIState,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onRestaurantRename: (String) -> Unit,
    onRestaurantNameValueChange: (String) -> Unit,
    onFavoriteRestaurantClick: (Restaurant) -> Unit,
    onKeyboardDone: (String) -> Unit,
    onVisitSelected: (Visit) -> Unit,
    onRemoveVisit: (Visit) -> Unit
) {
    ScreenScaffold(
        currentScreen = RestaurantInfo,
        onBack = onBack,
        onAdd = state.selectedRestaurant?.let { onAdd }
    ) { innerPadding ->
        state.selectedRestaurant?.let { currentSelectedRestaurant ->
            val isEditingRestaurantName = remember { mutableStateOf(false) }
            val (restaurantName, onRestaurantNameChange) = remember { mutableStateOf(currentSelectedRestaurant.name) }

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Screen title that includes the restaurant's name, edit icon, and favorite icon
                RestaurantInfoScreenTitle(
                    isEditing = isEditingRestaurantName.value,
                    onToggleEditingRestaurantName = {
                        // If the restaurant's name is being edited, rename the restaurant
                        if (isEditingRestaurantName.value) {
                            onRestaurantNameChange(restaurantName)
                            onRestaurantRename(restaurantName)
                        }
                        // Toggle the editing state
                        isEditingRestaurantName.value = !isEditingRestaurantName.value
                    },
                    restaurant = currentSelectedRestaurant,
                    restaurantInput = restaurantName,
                    onRestaurantNameChange = { newRestaurantName ->
                        onRestaurantNameChange(newRestaurantName)
                        onRestaurantNameValueChange(newRestaurantName)
                    },
                    isInputInvalid = state.isRestaurantRenameInputInvalid,
                    isRestaurantFavorite = currentSelectedRestaurant.isFavorite,
                    onFavoriteRestaurantClick = onFavoriteRestaurantClick,
                    onKeyboardDone = { onKeyboardDone(restaurantName) }
                )

                if(currentSelectedRestaurant.visits.isNotEmpty()) {
                    VisitsList(
                        visits = currentSelectedRestaurant.visits.reversed(),
                        onVisitSelected = onVisitSelected,
                        onRemoveVisit = onRemoveVisit
                    )
                } else {
                    NoVisitsList()
                }
            }
        } ?: NoRestaurantSelectedInfo(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun RestaurantInfoScreenTitle(
    isEditing: Boolean,
    onToggleEditingRestaurantName: () -> Unit,
    restaurant: Restaurant,
    restaurantInput: String,
    onRestaurantNameChange: (String) -> Unit,
    isInputInvalid: Boolean,
    isRestaurantFavorite: Boolean,
    onFavoriteRestaurantClick: (Restaurant) -> Unit,
    onKeyboardDone: () -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        EditableText(
            isBeingEdited = isEditing,
            text = restaurantInput,
            onTextChange = onRestaurantNameChange,
            isInputInvalid = isInputInvalid,
            label = if (isInputInvalid) {
                if (restaurantInput.isBlank()) {
                    stringResource(R.string.invalid_restaurant_name)
                } else {
                    stringResource(R.string.restaurant_name_exists)
                }
            } else {
                stringResource(R.string.enter_new_restaurant_name)
            },
            onKeyboardDone = onKeyboardDone
        )
        Row {
            if(isEditing) {
                AccessibleIcon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.submit_rename),
                    enabled = !isInputInvalid,
                    onClick = onToggleEditingRestaurantName
                )
            } else {
                AccessibleIcon(
                    imageVector = Icons.Default.Create,
                    contentDescription = stringResource(R.string.rename_restaurant),
                    onClick = onToggleEditingRestaurantName
                )
            }
            AccessibleIcon(
                imageVector = if(restaurant.isFavorite) {
                    Icons.Default.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                contentDescription = if(isRestaurantFavorite) {
                    stringResource(R.string.is_favorite)
                } else {
                    stringResource(R.string.is_not_favorite)
                },
                onClick = {
                    onFavoriteRestaurantClick(restaurant)
                }
            )
        }
    }
}

@Composable
private fun NoVisitsList() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.add_a_visit),
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = visit.name,
                style = Typography.titleLarge
            )
            Row {
                AccessibleIcon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = stringResource(R.string.visit_info),
                ) {
                    onVisitSelected(visit)
                }
                AccessibleIcon(
                    imageVector = Icons.Default.Close,
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
            visit.rating?.let {
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
fun NoRestaurantSelectedInfo(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
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

@Preview
@Composable
fun RestaurantInfoScreenPreview() {
    RestaurantInfoScreen(
        state = demoUIStateSelected,
        onBack = {},
        onAdd = {},
        onRestaurantRename = {},
        onRestaurantNameValueChange = {},
        onFavoriteRestaurantClick = {},
        onKeyboardDone = {},
        onVisitSelected = {},
        onRemoveVisit = {},
    )
}

@Preview
@Composable
fun RestaurantInfoScreenNoSelectionPreview() {
    RestaurantInfoScreen(
        state = demoUIState,
        onBack = {},
        onAdd = {},
        onRestaurantRename = {},
        onRestaurantNameValueChange = {},
        onFavoriteRestaurantClick = {},
        onKeyboardDone = {},
        onVisitSelected = {},
        onRemoveVisit = {},
    )
}