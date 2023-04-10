package com.piotrkalin.havira.android.dish.presentation.details.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InfoChip(
    text: String,
    leadingIcon : @Composable () -> Unit = {},
    outlined : Boolean = true
) {
    val modifier = if(outlined) Modifier
    .border(
        BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant),
        shape = MaterialTheme.shapes.small
    ) else Modifier
    Row(
        modifier = modifier
            /*.border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant),
                shape = MaterialTheme.shapes.small
            )*/
            .padding(vertical = 2.dp, horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingIcon()
        Text(text = text,
            modifier = Modifier
                .padding(horizontal = 6.dp),
            style = MaterialTheme.typography.labelLarge
        )
    }
    /*Text(text = text,
        modifier = Modifier
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant),
                shape = MaterialTheme.shapes.small
            )
            .padding(vertical = 6.dp, horizontal = 12.dp),
        style = MaterialTheme.typography.labelLarge
    )*/
}