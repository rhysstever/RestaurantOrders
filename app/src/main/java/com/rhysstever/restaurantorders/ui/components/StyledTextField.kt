package com.rhysstever.restaurantorders.ui.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isInputInvalid: Boolean?,
    label: String,
    onKeyboardDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(text = label) },
        isError = isInputInvalid ?: false,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { onKeyboardDone() }
        ),
        singleLine = true
    )
}

@Preview
@Composable
fun StyledTextFieldPreview() {
    val (value, onValueChange) = remember { mutableStateOf("") }

    StyledTextField(
        value = value,
        onValueChange = onValueChange,
        isInputInvalid = true,
        label = "Label",
        onKeyboardDone = {}
    )
}