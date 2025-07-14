package com.rhysstever.restaurantorders.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rhysstever.restaurantorders.ui.theme.Typography
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

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
                modifier = Modifier.padding(8.dp),
                style = Typography.bodyLarge
            )
        },
        showModeToggle = true,
        colors = DatePickerDefaults.colors().copy(
            containerColor = Color.Transparent
        )
    )
}

fun convertMillisToDate(millis: Long): LocalDate {
    val timeOffset = 43200000 // 12 hours in milliseconds
    val timeFormatter = SimpleDateFormat("yyyy/MM/dd", Locale.US)
    val stringDate = timeFormatter.format(Date(millis + timeOffset))
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    return LocalDate.parse(stringDate, dateFormatter)
}

fun displayDate(date: LocalDate): String {
    return date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CustomDatePickerPreview() {
    val state = rememberDatePickerState()

    Column {
        CustomDatePicker(
            state = state,
            headlineText = "Select a Date",
        )
        Text(
            text = "Selected Date: ${state.selectedDateMillis?.let {
                displayDate(convertMillisToDate(it))
            } ?: "Null"}"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CustomDatePickerInputPreview() {
    val state = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Input
    )

    Column {
        CustomDatePicker(
            state = state,
            headlineText = "Select a Date",
        )
        Text(
            text = "Selected Date: ${state.selectedDateMillis?.let {
                displayDate(convertMillisToDate(it))
            } ?: "Null"}"
        )
    }
}