package com.piotrkalin.havira.android.groupDish.presentation.list

import androidx.compose.runtime.Composable
import com.piotrkalin.havira.android.core.presentation.NavigationDrawer
import com.piotrkalin.havira.android.dish.presentation.list.DishBaseView
import com.piotrkalin.havira.android.dish.presentation.list.DishSearchView
import com.piotrkalin.havira.core.presentation.NavigationDrawerEvent
import com.piotrkalin.havira.core.presentation.NavigationDrawerState
import com.piotrkalin.havira.dish.presentation.list.DishListEvent
import com.piotrkalin.havira.dish.presentation.list.DishListState

@Composable
fun GroupDishListScreen(
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