package com.example.havira.android.dish.details.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DishPrepCard(
    rating: Int,
    date: String,
    modifier : Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column() { 
            RatingStars(rating = rating)
            Text(text = "Made: $date")
        }
    }
}

@Composable
fun RatingStars(rating: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..5) {
            val icon = if (i <= rating) {
                Icons.Filled.Star
                //Icons.Filled.Cookie
            } else {
                Icons.Outlined.StarOutline
                //Icons.Outlined.Cookie
            }
            val starSize = if (i <= rating) 46.dp else 46.dp
            Icon(imageVector = icon, modifier = Modifier.size(starSize), contentDescription = "")
        }
    }
}