package com.example.havira.android.dish.details.presentation

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cookie
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.havira.android.dish.details.presentation.components.DishPrepCard
import com.example.havira.android.dish.details.presentation.components.DishPrepCreator
import com.example.havira.android.dish.details.presentation.components.InfoChip
import com.example.havira.android.dish.details.presentation.components.TitleCard
import com.example.havira.core.domain.util.DateTimeUtil
import com.example.havira.dish.presentation.detail.DishDetailEvent
import com.example.havira.dish.presentation.detail.DishDetailState
import kotlin.math.roundToInt

@Composable
fun DishDetailScreen(
    state : DishDetailState,
    onEvent : (DishDetailEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .alpha(if (state.isDishPrepCreatorOpen) 0.7f else 1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        state.dish?.let {
            with(it){
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    item {
                        TitleCard(title = title)
                        Spacer(modifier = Modifier.padding(4.dp))
                        DishDetailInfoCard(
                            lastMade = lastMade?.let { lastMade -> DateTimeUtil.formatDate(lastMade) } ?: "Never",
                            rating = "%.1f".format(rating),
                            nofRatings = nofRatings.toString(),
                            dishDescription = desc
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                    }
                    items(dishPreps ?: emptyList()){ dishPrep ->
                        DishPrepCard(rating = dishPrep.rating, date = DateTimeUtil.formatDate(dishPrep.date))
                    }
                }
            }
        }
    }
    if(state.isDishPrepCreatorOpen) DishDetailDishPrepCreator(state = state, onEvent = onEvent)
    else DishDetailFloatingActionButton(state = state, onEvent = onEvent)

}

@Composable
fun DishDetailDishPrepCreator(
    state: DishDetailState,
    onEvent: (DishDetailEvent) -> Unit
){
    var offsetY by remember { mutableStateOf(0f) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        DishPrepCreator(
            date = state.newDishPrepDate,
            rating = state.newDishPrepRating,
            onEvent = onEvent,
            modifier = Modifier
                .offset { IntOffset(0, offsetY.roundToInt()) }
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        println(offsetY)
                        if (offsetY + delta > 0) offsetY += delta
                        //if (offsetY > 500) onEvent(DishDetailEvent.CloseDishPrepCreator)
                    },
                    onDragStopped = {
                        if (offsetY > 450) onEvent(DishDetailEvent.CloseDishPrepCreator)
                        else offsetY = 0f
                    }
                )
        )
    }
}

@Composable
fun DishDetailFloatingActionButton(
    state: DishDetailState,
    onEvent: (DishDetailEvent) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        LargeFloatingActionButton(onClick = {
            if(state.isDishPrepCreatorOpen) onEvent(DishDetailEvent.CloseDishPrepCreator)
            else onEvent(DishDetailEvent.OpenDishPrepCreator)
        })
        {
            Icon(
                imageVector = Icons.Rounded.Cookie,
                contentDescription = "",
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
fun DishDetailInfoCard(
    lastMade : String,
    rating: String,
    nofRatings: String,
    dishDescription: String
){
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 200.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                InfoChip(text = "Last made: $lastMade")
                InfoChip(text = "Made: $nofRatings times")
                InfoChip(text = "Score: $rating")
            }
            Divider(
                modifier = Modifier.padding(bottom = 16.dp, top = 8.dp),
                color = MaterialTheme.colorScheme.background
            )
            Text(dishDescription)
        }
    }
}

/*
Szczegóły dania:
Tytuł - text, textCard?
Chips:
    Ocena - icon, pill?
    Kiedy ostatnio zrobione - text, ???
    Ile razy zrobione - text, ???
Opis - text, textCard?

Kiedy dodane - text, ???
Lista dat i ocen:
    Data - text
    Ocena - icon

Danie zrobione - button, Floating centered button?
*/
