package com.example.havira.android.dish.details.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.rounded.Cookie
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.havira.core.domain.util.DateTimeUtil
import com.example.havira.dish.presentation.detail.DishDetailEvent
import kotlinx.datetime.LocalDateTime

@Composable
fun DishPrepCreator(
    date: LocalDateTime,
    rating: Int,
    onEvent: (DishDetailEvent) -> Unit,
    modifier : Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(0.8f),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 12.dp
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(4.dp)
        ) {
            Spacer(modifier = Modifier.padding(4.dp))
            Box(modifier = Modifier
                .width(50.dp)
                .height(4.dp)
                .background(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ))
            DishPrepDatePicker(date = date)
            RatingStarsPicker(rating = rating, onEvent = onEvent)
            LargeFloatingActionButton(onClick = {onEvent(DishDetailEvent.AddDishPrep)})
            {
                Icon(
                    imageVector = Icons.Rounded.Cookie,
                    contentDescription = "",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Composable
fun DishPrepDatePicker(
    date: LocalDateTime
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = DateTimeUtil.formatDate(date))
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Filled.DateRange, contentDescription = "")
        }
    }
}

@Composable
fun RatingStarsPicker(
    rating: Int,
    onEvent: (DishDetailEvent) -> Unit,
){
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            val icon = if (i <= rating) {
                Icons.Filled.Star
            } else {
                Icons.Outlined.StarOutline
            }
            val starSize = if (i <= rating) 54.dp else 54.dp
            Icon(
                imageVector = icon,
                contentDescription = "",
                modifier = Modifier
                    .size(starSize)
                    .clickable {
                        onEvent(DishDetailEvent.SetDishPrepCreatorRating(i))
                    }
                )
        }
    }
}

