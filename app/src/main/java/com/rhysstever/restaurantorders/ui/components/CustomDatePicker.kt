package com.rhysstever.restaurantorders.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    state: DatePickerState,
    headlineText: String,
    modifier: Modifier = Modifier,
) {
    DatePicker(
        state = state,
        modifier = modifier,
        title = null,
        headline = {
            Text(
                text = headlineText,
                modifier = Modifier.padding(8.dp)
            )
        },
        showModeToggle = true,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CustomDatePickerPreview() {
    val state = rememberDatePickerState()

    CustomDatePicker(
        state = state,
        headlineText = "Select a Date",
    )
}