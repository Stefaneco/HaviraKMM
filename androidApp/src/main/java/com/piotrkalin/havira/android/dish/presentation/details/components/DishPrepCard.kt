package com.piotrkalin.havira.android.dish.presentation.details.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun DishPrepCard(
    rating: Int,
    date: String,
    modifier : Modifier = Modifier,
    uid: String? = null,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)
        ) {
            Column() {
                RatingStars(rating = rating)
                Text(text = "Made: $date")
            }
            uid?.let {
                AsyncImage(
                    model = "https://havirastorage.blob.core.windows.net/profilepictures/$it",
                    contentDescription = "",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.dp)
                        .border(1.dp, Color.Gray, CircleShape)
                )
            }
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