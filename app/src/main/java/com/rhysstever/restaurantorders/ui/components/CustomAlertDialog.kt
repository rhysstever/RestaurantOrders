package com.rhysstever.restaurantorders.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.rhysstever.restaurantorders.R
import com.rhysstever.restaurantorders.ui.theme.Typography

@Composable
fun CustomAlertDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            ButtonFill(
                text = stringResource(R.string.confirm),
                onClick = onConfirm
            )
        },
        modifier = modifier,
        dismissButton = {
            ButtonFill(
                text = stringResource(R.string.cancel),
                onClick = onDismiss
            )
        },
        title = {
            Text(
                text = title,
                style = Typography.titleLarge
            )
        },
        text = {
            Text(
                text = body,
                style = Typography.bodyLarge
            )
        }
    )
}

@Preview
@Composable
fun CustomAlertDialogPreview() {
    CustomAlertDialog(
        onDismiss = {},
        onConfirm = {},
        title = "Delete visit?",
        body = "Are you sure you want to delete this visit? This action cannot be undone."
    )
}