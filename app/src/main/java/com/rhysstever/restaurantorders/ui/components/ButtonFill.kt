package com.rhysstever.restaurantorders.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rhysstever.restaurantorders.ui.theme.AppIcon
import com.rhysstever.restaurantorders.ui.theme.AppIcons
import com.rhysstever.restaurantorders.ui.theme.Typography

@Composable
fun ButtonFill(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: AppIcons? = null,
    trailingIcon: AppIcons? = null,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.requiredHeightIn(min = 48.dp),
        enabled = enabled,
    ) {
        leadingIcon?.let {
            AppIcon(
                icon = it,
                modifier = Modifier.padding(12.dp)
            )
        }
        Text(
            text = text,
            style = Typography.bodyLarge,
            modifier = Modifier.weight(weight= 1f, fill = false)
        )
        trailingIcon?.let {
            AppIcon(
                icon = it,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Preview
@Composable
fun ButtonFillPreview() {
    Column {
        ButtonFill(
            text = "Basic Button",
            onClick = {}
        )
        ButtonFill(
            text = "Button with Two Custom Icons",
            onClick = {},
            leadingIcon = AppIcons.Add,
            trailingIcon = AppIcons.FavoriteOutline
        )
        ButtonFill(
            text = "Button with Two Custom Icons and this is a very long text",
            onClick = {},
            leadingIcon = AppIcons.Add,
            trailingIcon = AppIcons.FavoriteOutline
        )
    }
}