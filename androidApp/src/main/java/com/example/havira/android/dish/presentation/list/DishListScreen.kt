@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.havira.android.dish.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.example.havira.core.domain.util.DateTimeUtil
import com.example.havira.dish.presentation.list.DishListEvent
import com.example.havira.dish.presentation.list.DishListState

@Composable
fun DishListScreen(
    state: DishListState,
    onEvent : (DishListEvent) -> Unit
) {
    if(state.isSearchViewOpen)
        DishSearchView(
            searchText = state.searchText,
            onSearchTextChange = { onEvent(DishListEvent.SearchDish(it)) },
            onDismiss = { onEvent(DishListEvent.DismissSearchView) },
            onItemSelected = { onEvent(DishListEvent.SelectDish(it)) },
            items = state.searchedDishes
        )
    else
        DishBaseView(state = state, onEvent = onEvent)
}

@Composable
fun DishBaseView(
    state: DishListState,
    onEvent: (DishListEvent) -> Unit
){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(modifier = Modifier.fillMaxWidth(), text = "Dishes") },
                actions = {
                    IconButton(onClick = { onEvent(DishListEvent.OpenSearchView) }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Localized description"
                        )
                    }
                    IconButton(onClick = { onEvent(DishListEvent.ChangeFilterBoxVisibility) }) {
                        Icon(
                            //imageVector = Icons.Filled.FilterList,
                            imageVector = Icons.Filled.Tune,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            if(state.isFilterBoxVisible) DishFilterBox(state = state, onEvent = onEvent, scrollBehavior = scrollBehavior)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(vertical = 4.dp, horizontal = 4.dp)
            ){
                items(state.sortedDishes) { dish ->
                    DishCard(
                        headline = dish.title,
                        supportingText =  dish.lastMade?.let { DateTimeUtil.formatDate(it) } ?: "New dish",
                        supportingText2 = "${dish.nofRatings} ratings",
                        trailingSupportingText = "%.1f".format(dish.rating),
                        modifier = Modifier.clickable { onEvent(DishListEvent.SelectDish(dish.id!!)) })
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {onEvent(DishListEvent.CreateDish)} ) {
                Text("New Dish")
            }
        }
    }
}