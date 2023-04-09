@file:OptIn(ExperimentalMaterial3Api::class)

package com.piotrkalin.havira.android.dish.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.piotrkalin.havira.android.R
import com.piotrkalin.havira.android.core.presentation.NavigationDrawer
import com.piotrkalin.havira.core.domain.util.DateTimeUtil
import com.piotrkalin.havira.core.presentation.NavigationDrawerEvent
import com.piotrkalin.havira.core.presentation.NavigationDrawerState
import com.piotrkalin.havira.dish.presentation.list.DishListEvent
import com.piotrkalin.havira.dish.presentation.list.DishListState

@Composable
fun DishListScreen(
    state: DishListState,
    onEvent : (DishListEvent) -> Unit,
    navDrawerState : NavigationDrawerState,
    navDrawerOnEvent : (NavigationDrawerEvent) -> Unit
) {
    if(state.isSearchViewOpen)
        DishSearchView(
            searchText = state.searchText,
            onSearchTextChange = { onEvent(DishListEvent.SearchDish(it)) },
            onDismiss = { onEvent(DishListEvent.DismissSearchView) },
            onItemSelected = { onEvent(DishListEvent.SelectDish(it)) },
            items = state.searchedDishes
        )
    else {
        NavigationDrawer(state = navDrawerState, onEvent = navDrawerOnEvent) {
            DishBaseView(state = state, onEvent = onEvent, onMenuPressed = {
                navDrawerOnEvent(NavigationDrawerEvent.OpenDrawer)
            })
        }
    }
}

@Composable
fun DishBaseView(
    state: DishListState,
    onEvent: (DishListEvent) -> Unit,
    onMenuPressed : () -> Unit,
    additionalActions : @Composable () -> Unit = {}
){
    //val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = state.groupName ?: stringResource(id = R.string.title_list_of_dishes)
                ) },
                navigationIcon = {
                    IconButton(onClick = { onMenuPressed() }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(id = R.string.menu_button_description)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onEvent(DishListEvent.OpenSearchView) }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(id = R.string.search_dishes_button_description)
                        )
                    }
                    IconButton(onClick = { onEvent(DishListEvent.ChangeFilterBoxVisibility) }) {
                        Icon(
                            imageVector = Icons.Filled.Tune,
                            contentDescription = stringResource(id = R.string.tune_dishes_button_description)
                        )
                    }
                    additionalActions()
                },
                scrollBehavior = scrollBehavior)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            if(state.isFilterBoxVisible) DishFilterBox(state = state, onEvent = onEvent, scrollBehavior = scrollBehavior)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(vertical = 4.dp, horizontal = 4.dp)
            ){
                items(state.sortedDishes) { dish ->
                    DishCard(
                        headline = dish.title,
                        supportingText =  dish.lastMade?.let { DateTimeUtil.formatDate(it) }
                            ?: stringResource(id = R.string.last_made_new_dish),
                        supportingText2 = stringResource(id = R.string.x_ratings, dish.nofRatings),
                        trailingSupportingText = "%.1f".format(dish.rating),
                        modifier = Modifier.clickable { onEvent(DishListEvent.SelectDish(dish.id!!)) })
                }
                /*items(20){
                    DishCard(
                        headline = "dish.title",
                        supportingText = stringResource(id = R.string.last_made_new_dish),
                        supportingText2 = stringResource(id = R.string.x_ratings, 3),
                        trailingSupportingText = "%.1f".format(4.5f),
                        modifier = Modifier.clickable {  })
                }*/
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {onEvent(DishListEvent.CreateDish)} ) {
                Text(stringResource(id = R.string.add_new_dish_button_label))
            }
        }
    }
}