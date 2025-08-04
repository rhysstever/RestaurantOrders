package com.rhysstever.restaurantorders.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rhysstever.restaurantorders.R
import com.rhysstever.restaurantorders.ui.theme.Typography

@Composable
fun RatingsRow(
    rating: Int?,
    onRatingChanged: (Int) -> Unit,
    ratingTitle: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val clampedRating = rating?.coerceIn(1, 5) ?: 0

    Column(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {}
            .clickable(
                enabled = enabled,
                role = Role.Button
            ) {
                if(clampedRating == 5) {
                    onRatingChanged(0) // Reset rating if already at max
                } else {
                    onRatingChanged(clampedRating + 1)
                }
            }
    ) {
        Text(
            text = ratingTitle,
            style = Typography.bodyLarge,
        )

        val contentDescription = pluralStringResource(R.plurals.stars, clampedRating, clampedRating)

        Row(
            modifier = Modifier
                .semantics {
                    this.contentDescription = contentDescription
                }
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(5) { index ->
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    modifier = Modifier
                        .requiredSizeIn(
                            minWidth = 24.dp,
                            minHeight = 24.dp
                        )
                        .padding(12.dp),
                    tint = if (index < clampedRating) {
                        Color(0xFFFFBB00)
                    } else {
                        Color.Black
                    },
                )
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
        onRatingChanged = { newRating ->
            rating = if(newRating == 0) {
                null
            } else {
                newRating
            }
        },
        ratingTitle = "Rating Title",
    )
}