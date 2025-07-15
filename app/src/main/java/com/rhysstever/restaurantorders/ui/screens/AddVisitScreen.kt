package com.rhysstever.restaurantorders.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rhysstever.restaurantorders.AddVisit
import com.rhysstever.restaurantorders.R
import com.rhysstever.restaurantorders.ui.Restaurant
import com.rhysstever.restaurantorders.ui.RestaurantUIState
import com.rhysstever.restaurantorders.ui.Visit
import com.rhysstever.restaurantorders.ui.components.CustomDatePicker
import com.rhysstever.restaurantorders.ui.components.RatingsRow
import com.rhysstever.restaurantorders.ui.components.ScreenScaffold
import com.rhysstever.restaurantorders.ui.components.StyledTextField
import com.rhysstever.restaurantorders.ui.components.convertDateToMillis
import com.rhysstever.restaurantorders.ui.components.convertMillisToDate
import java.util.Calendar

@Composable
fun AddVisitScreen(
    state: RestaurantUIState,
    onBack: () -> Unit,
    onNewVisitInput: (String) -> Unit,
    onKeyboardDone: (String) -> Unit,
    onReplaceVisit: (Restaurant, Visit, Visit) -> Unit,
    onAddNewVisit: (Restaurant, Visit) -> Unit
) {
    ScreenScaffold(
        currentScreen = AddVisit,
        onBack = onBack,
        onAdd = null,
    ) { innerPadding ->
        state.selectedRestaurant?.let { currentlySelectedRestaurant ->
            AddVisitScreenContent(
                restaurant = currentlySelectedRestaurant,
                visit = state.selectedVisit,
                isVisitNameInputInvalid = state.isNewVisitInputInvalid,
                onNewVisitInput = onNewVisitInput,
                onKeyboardDone = onKeyboardDone,
                onReplaceVisit = onReplaceVisit,
                onAddNewVisit = onAddNewVisit,
                modifier = Modifier.padding(innerPadding)
            )
        } ?: NoSelectedRestaurantMessage(modifier = Modifier.padding(innerPadding))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddVisitScreenContent(
    restaurant: Restaurant,
    visit: Visit?,
    isVisitNameInputInvalid: Boolean?,
    onNewVisitInput: (String) -> Unit,
    onKeyboardDone: (String) -> Unit,
    onReplaceVisit: (Restaurant, Visit, Visit) -> Unit,
    onAddNewVisit: (Restaurant, Visit) -> Unit,
    modifier: Modifier = Modifier
) {
    val (visitName, onVisitNameChange) = remember { mutableStateOf(visit?.name ?: "") }
    var rating by remember { mutableStateOf(visit?.rating) }

    val calendar = Calendar.getInstance()
    val dateVisited = rememberDatePickerState(
        initialSelectedDateMillis = visit?.dateVisited?.let { convertDateToMillis(it) },
        initialDisplayMode = DisplayMode.Input,
        // Restrict year selection to any year at or before the current year
        yearRange = IntRange(DatePickerDefaults.YearRange.first, calendar.get(Calendar.YEAR))
    )
    val (notes, onNotesValueChange) = remember { mutableStateOf(visit?.notes ?: "") }

    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Visit name text field
        StyledTextField(
            value = visitName,
            onValueChange = { newVisitName ->
                onVisitNameChange(newVisitName)
                onNewVisitInput(newVisitName)
            },
            isInputInvalid = isVisitNameInputInvalid,
            label = isVisitNameInputInvalid?.let { isInvalid ->
                if (isInvalid) {
                    stringResource(R.string.invalid_visit_name)
                } else {
                    stringResource(R.string.enter_visit_name)
                }
            } ?: stringResource(R.string.enter_visit_name),
            onKeyboardDone = { onKeyboardDone(visitName) },
            modifier = Modifier.fillMaxWidth()
        )

        RatingsRow(
            rating = rating,
            onRatingChanged = {
                rating = if(it == rating) { null } else { it }
            },
            ratingTitle = stringResource(R.string.visit_rating)
        )

        CustomDatePicker(
            state = dateVisited,
            headlineText = stringResource(R.string.date_visited_picker_header)
        )

        // Notes text field
        StyledTextField(
            value = notes,
            onValueChange = onNotesValueChange,
            isInputInvalid = false,
            label = stringResource(R.string.add_notes),
            onKeyboardDone = { },
            modifier = Modifier.fillMaxWidth()
        )

        // Submit button
        Button(
            onClick = {
                // Create a new Visit object
                val newVisit = Visit(
                    name = visitName,
                    rating = rating,
                    notes = notes,
                    dateVisited = dateVisited.selectedDateMillis?.let { convertMillisToDate(it) }
                )

                // If a current visit is selected, replace it with the new selections,
                // Otherwise create a new visit
                visit?.let { oldVisit ->
                    onReplaceVisit(restaurant, oldVisit, newVisit)
                } ?: onAddNewVisit(restaurant, newVisit)
            },
            enabled = isVisitNameInputInvalid?.let { !it } ?: false,
        ) {
            Text(text = visit?.let {
                stringResource(R.string.update_visit)
            } ?: stringResource(R.string.add_visit))
        }
    }
}