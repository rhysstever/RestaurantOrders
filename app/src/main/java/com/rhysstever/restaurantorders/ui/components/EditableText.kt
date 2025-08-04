package com.rhysstever.restaurantorders.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rhysstever.restaurantorders.ui.theme.Typography

@Composable
fun EditableText(
    isBeingEdited: Boolean,
    text: String,
    onTextChange: (String) -> Unit,
    isInputInvalid: Boolean?,
    label: String,
    onKeyboardDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.requiredHeightIn(min = 56.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        if(isBeingEdited) {
            StyledTextField(
                value = text,
                onValueChange = onTextChange,
                isInputInvalid = isInputInvalid,
                label = label,
                onKeyboardDone = onKeyboardDone
            )
        } else {
            Text(
                text = text,
                overflow = TextOverflow.Visible,
                style = Typography.headlineLarge,
            )
        }
    }
}

@Preview
@Composable
fun EditableTextPreview() {
    val (value, onValueChange) = remember { mutableStateOf("Text") }
    val isEditing = remember { mutableStateOf(false) }

    Column {
        ButtonFill(
            text = "Toggle Editing",
            onClick = { isEditing.value = !isEditing.value },
        )
        EditableText(
            isBeingEdited = isEditing.value,
            text = value,
            onTextChange = onValueChange,
            isInputInvalid = null,
            label = "Label",
            onKeyboardDone = {}
        )
    }
}