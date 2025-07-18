package com.rhysstever.restaurantorders.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun AccessibleIcon(
    imageVector: ImageVector,
    contentDescription: String,
    tint: Color = LocalContentColor.current,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable(
                enabled = enabled,
                onClickLabel = onClickLabel,
                role = Role.Button,
                onClick = onClick
            )
            .requiredSizeIn(
                minWidth = 48.dp,
                minHeight = 48.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.requiredSizeIn(
                minWidth = 24.dp,
                minHeight = 24.dp
            ),
            tint = tint
        )
    }
}