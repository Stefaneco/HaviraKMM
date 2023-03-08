package com.example.havira.android.dish.presentation.details.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InfoChip(
    text: String
) {
    Text(text = text,
        modifier = Modifier
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant),
                shape = MaterialTheme.shapes.small
            )
            .padding(vertical = 6.dp, horizontal = 12.dp),
        style = MaterialTheme.typography.labelLarge
    )
}