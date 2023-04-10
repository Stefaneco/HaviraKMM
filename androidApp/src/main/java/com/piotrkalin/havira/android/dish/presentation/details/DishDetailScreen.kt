package com.piotrkalin.havira.android.dish.presentation.details

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Cookie
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.piotrkalin.havira.android.R
import com.piotrkalin.havira.android.dish.presentation.details.components.DishPrepCard
import com.piotrkalin.havira.android.dish.presentation.details.components.DishPrepCreator
import com.piotrkalin.havira.android.dish.presentation.details.components.InfoChip
import com.piotrkalin.havira.core.domain.util.DateTimeUtil
import com.piotrkalin.havira.dish.presentation.detail.DishDetailEvent
import com.piotrkalin.havira.dish.presentation.detail.DishDetailState
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishDetailScreen(
    state : DishDetailState,
    onEvent : (DishDetailEvent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = state.dish?.title ?: "",
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(DishDetailEvent.BackButtonPressed) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button_description)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                        state.dish?.id?.let { onEvent(DishDetailEvent.EditButtonPressed(it))  }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = stringResource(id = R.string.edit_dish_button_description)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
           )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                            //TitleCard(title = title)
                            Spacer(modifier = Modifier.padding(4.dp))
                            DishDetailInfoCard(
                                lastMade = lastMade?.let { lastMade -> DateTimeUtil.formatDate(lastMade) }
                                    ?: stringResource(id = R.string.last_made_date_never),
                                rating = "%.1f".format(rating),
                                nofRatings = nofRatings.toString(),
                                dishDescription = desc
                            )
                            Spacer(modifier = Modifier.padding(4.dp))
                        }
                        items(dishPreps ?: emptyList()){ dishPrep ->
                            DishPrepCard(
                                rating = dishPrep.rating,
                                date = DateTimeUtil.formatDate(dishPrep.date),
                                uid = dishPrep.userId
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.padding(50.dp))
                        }
                    }
                }
            }
        }
        state.error?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it)
                onEvent(DishDetailEvent.OnErrorSeen)
            }
        }
        if(state.isDishPrepCreatorOpen) DishDetailDishPrepCreator(state = state, onEvent = onEvent)
        else DishDetailFloatingActionButton(state = state, onEvent = onEvent)
    }



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
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 200.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                /*InfoChip(text = stringResource(R.string.last_made_x_info_chip, lastMade), )
                InfoChip(text = stringResource(R.string.made_x_times_info_chip, nofRatings))
                InfoChip(text = stringResource(R.string.score_x_info_chip, rating))*/

                InfoChip(
                    text = lastMade,
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Event, contentDescription = "")
                    },
                    outlined = false
                )
                InfoChip(
                    text = nofRatings,
                    leadingIcon = {
                        //Refresh, Done, DoneAll
                        Icon(imageVector = Icons.Default.DoneAll, contentDescription = "")
                    },
                    outlined = false
                )
                InfoChip(
                    text = rating,
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Star, contentDescription = "")
                    },
                    outlined = false
                )
            }
            Divider(
                modifier = Modifier.padding(bottom = 16.dp, top = 8.dp),
                color = MaterialTheme.colorScheme.background
            )
            Text(text = dishDescription, modifier = Modifier.padding(horizontal = 8.dp))
        }
    }
}
