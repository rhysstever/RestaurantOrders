package com.rhysstever.restaurantorders.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
            Button(onClick = onConfirm) {
                Text(text = stringResource(R.string.confirm))
            }
        },
        modifier = modifier,
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
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