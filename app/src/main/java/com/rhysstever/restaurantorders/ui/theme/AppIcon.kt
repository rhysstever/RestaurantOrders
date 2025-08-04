package com.rhysstever.restaurantorders.ui.theme

import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rhysstever.restaurantorders.R

enum class AppIcons(val drawableId: Int) {
    Add(R.drawable.add),
    Check(R.drawable.check),
    ChevronLeft(R.drawable.chevron_left),
    ChevronRight(R.drawable.chevron_right),
    Close(R.drawable.close),
    EditFilled(R.drawable.edit_filled),
    EditOutline(R.drawable.edit_outline),
    FavoriteFilled(R.drawable.favorite_filled),
    FavoriteOutline(R.drawable.favorite_outline),
    InfoFilled(R.drawable.info_filled),
    InfoOutline(R.drawable.info_outline),
    StarFilled(R.drawable.star_filled),
    StarOutline(R.drawable.star_outline),
}

@Composable
fun AppIcon(
    icon: AppIcons,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current
) {
    androidx.compose.material3.Icon(
        painter = painterResource(id = icon.drawableId),
        contentDescription = contentDescription,
        modifier = modifier
            .requiredSizeIn(
                minWidth = 24.dp,
                minHeight = 24.dp
            ),
        tint = tint
    )
}