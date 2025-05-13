package com.rhysstever.restaurantorders.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RatingsRow(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val clampedRating = rating.coerceIn(0, 5)
    Row(
        modifier = modifier
    ) {
        repeat(5) { index ->
            AccessibleIcon(
                imageVector = Icons.Filled.Star,
                tint = if (index < clampedRating) {
                    Color(0xFFFFBB00)
                } else {
                    Color.Black
                },
                contentDescription = "$index Stars"
            ) {
                onRatingChanged(index + 1)
            }
        }
    }
}

@Preview
@Composable
fun RatingsRowPreview() {
    var rating by remember { mutableIntStateOf(3) }

    RatingsRow(
        rating = rating,
        onRatingChanged = {
            rating = if(it == rating) {
                0
            } else {
                it
            }
        }
    )
}