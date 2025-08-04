package com.rhysstever.restaurantorders.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rhysstever.restaurantorders.R
import com.rhysstever.restaurantorders.ui.RestaurantViewModel
import com.rhysstever.restaurantorders.ui.Visit
import com.rhysstever.restaurantorders.ui.components.ButtonFill
import com.rhysstever.restaurantorders.ui.components.CustomDatePicker
import com.rhysstever.restaurantorders.ui.components.RatingsRow
import com.rhysstever.restaurantorders.ui.components.ScreenScaffold
import com.rhysstever.restaurantorders.ui.components.StyledTextField
import com.rhysstever.restaurantorders.ui.components.convertDateToMillis
import com.rhysstever.restaurantorders.ui.components.convertMillisToDate
import com.rhysstever.restaurantorders.ui.navigation.AddVisit
import com.rhysstever.restaurantorders.ui.navigation.RestaurantInfo
import com.rhysstever.restaurantorders.ui.navigation.VisitInfo
import java.util.Calendar

@Composable
fun AddVisitScreen(
    navController: NavController,
    restaurantViewModel: RestaurantViewModel
) {
    val uiState by restaurantViewModel.uiState.collectAsState()

    ScreenScaffold(
        currentScreen = AddVisit,
        onBack = { navController.navigate(RestaurantInfo.route) },
    ) { innerPadding ->
        uiState.selectedRestaurant?.let { _ ->
            AddVisitScreenContent(
                visit = uiState.selectedVisit,
                isVisitNameInputInvalid = uiState.isNewVisitInputInvalid,
                onNewVisitInput = { newVisitName ->
                    restaurantViewModel.VisitContent().checkNewVisitInput(newVisitName)
                },
                onKeyboardDone = { newVisitName ->
                    restaurantViewModel.VisitContent().checkNewVisitInput(newVisitName)
                },
                onEditVisit = { updatedVisit ->
                    restaurantViewModel.VisitContent().editSelectedVisit(updatedVisit = updatedVisit)
                    navController.navigate(VisitInfo.route)
                },
                onAddNewVisit = { visit ->
                    restaurantViewModel.VisitContent().addVisit(visit)
                    navController.navigate(RestaurantInfo.route)
                },
                modifier = Modifier.padding(innerPadding)
            )
        } ?: NoSelectedRestaurantMessage(modifier = Modifier.padding(innerPadding))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddVisitScreenContent(
    visit: Visit?,
    isVisitNameInputInvalid: Boolean?,
    onNewVisitInput: (String) -> Unit,
    onKeyboardDone: (String) -> Unit,
    onEditVisit: (Visit) -> Unit,
    onAddNewVisit: (Visit) -> Unit,
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
        ButtonFill(
            text = visit?.let {
                stringResource(R.string.update_visit)
            } ?: stringResource(R.string.add_visit),
            onClick = {
                // Create a new Visit object
                val newVisit = Visit(
                    name = visitName,
                    orders = visit?.orders ?: emptyList(),
                    rating = rating,
                    notes = notes,
                    dateVisited = dateVisited.selectedDateMillis?.let { convertMillisToDate(it) }
                )

                // If a current visit is selected, edit it with the new selections,
                // Otherwise create a new visit
                visit?.let { _ ->
                    onEditVisit(newVisit)
                } ?: onAddNewVisit(newVisit)
            },
            enabled = isVisitNameInputInvalid?.let { !it } ?: false,
        )
    }
}

@Preview
@Composable
private fun AddVisitScreenPreview() {
    AddVisitScreenContent(
        visit = null,
        isVisitNameInputInvalid = false,
        onNewVisitInput = {},
        onKeyboardDone = {},
        onEditVisit = {},
        onAddNewVisit = {}
    )
}