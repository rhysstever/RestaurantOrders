package com.rhysstever.restaurantorders.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rhysstever.restaurantorders.R

@Composable
fun RestaurantInfoScreenTitle(
    isEditing: Boolean,
    onToggleEditingRestaurantName: () -> Unit,
    restaurantInput: String,
    onRestaurantNameChange: (String) -> Unit,
    isInputInvalid: Boolean,
    isRestaurantFavorite: Boolean,
    onFavoriteClick: () -> Unit,
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
                imageVector = if(isRestaurantFavorite) {
                    Icons.Default.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                contentDescription = if(isRestaurantFavorite) {
                    stringResource(R.string.is_favorite)
                } else {
                    stringResource(R.string.is_not_favorite)
                },
                onClick = onFavoriteClick
            )
        }
    }
}