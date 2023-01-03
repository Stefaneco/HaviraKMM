package com.example.havira.android.dish.list.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DishCard(
    headline : String,
    supportingText : String,
    trailingSupportingText : String,
    modifier : Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(4.dp).fillMaxWidth(0.8f)
            ) {
                Text(text = headline, style = MaterialTheme.typography.headlineSmall)
                Text(text = supportingText, style = MaterialTheme.typography.bodySmall)
            }
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = trailingSupportingText)
        }
    }
}