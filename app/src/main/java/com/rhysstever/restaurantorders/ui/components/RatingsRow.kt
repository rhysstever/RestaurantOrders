package com.rhysstever.restaurantorders.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.rhysstever.restaurantorders.R
import com.rhysstever.restaurantorders.ui.theme.Typography

@Composable
fun RatingsRow(
    rating: Int?,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val clampedRating = rating?.coerceIn(1, 5) ?: run { 0 }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.order_rating),
            style = Typography.bodyLarge,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(5) { index ->
                AccessibleIcon(
                    imageVector = Icons.Filled.Star,
                    tint = if (index < clampedRating) {
                        Color(0xFFFFBB00)
                    } else {
                        Color.Black
                    },
                    contentDescription = pluralStringResource(R.plurals.stars, index, index)
                ) {
                    onRatingChanged(index + 1)
                }
            }
        }
    }
}

@Preview
@Composable
fun RatingsRowPreview() {
    var rating by remember { mutableStateOf<Int?>(3) }

    RatingsRow(
        rating = rating,
        onRatingChanged = {
            rating = if(it == rating) {
                null
            } else {
                it
            }
        }
    )
}