package com.piotrkalin.havira.android.groupDish.presentation.list

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.piotrkalin.havira.android.core.presentation.NavigationDrawer
import com.piotrkalin.havira.android.dish.presentation.list.DishBaseView
import com.piotrkalin.havira.android.dish.presentation.list.DishSearchView
import com.piotrkalin.havira.core.presentation.NavigationDrawerEvent
import com.piotrkalin.havira.core.presentation.NavigationDrawerState
import com.piotrkalin.havira.dish.presentation.list.DishListEvent
import com.piotrkalin.havira.dish.presentation.list.DishListState
import com.piotrkalin.havira.groupDish.presentation.list.GroupDishListEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDishListScreen(
    state: DishListState,
    onEvent : (DishListEvent) -> Unit,
    groupOnEvent: (GroupDishListEvent) -> Unit,
    navDrawerState : NavigationDrawerState,
    navDrawerOnEvent : (NavigationDrawerEvent) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    /*val sendIntent = remember {
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, state.groupJoinCode)
            type = "text/plain"
        }
    }*/

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
            DishBaseView(
                state = state,
                onEvent = onEvent,
                onMenuPressed = {
                navDrawerOnEvent(NavigationDrawerEvent.OpenDrawer)
            },
                additionalActions = {
                    IconButton(onClick = { groupOnEvent(GroupDishListEvent.OpenBottomSheet) }) {
                        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Options")
                    }
                })
            if(state.isBottomSheetOpen) {
                ModalBottomSheet(
                    onDismissRequest = { groupOnEvent(GroupDishListEvent.CloseBottomSheet) },
                    sheetState = bottomSheetState
                ) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                    ){
                        TextButton(
                            //modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, state.groupJoinCode)
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, null)
                                launcher.launch(shareIntent)
                                //GroupDishListEvent.ShareJoinCode
                            }) {
                            Text(text = "Share Join Code")
                        }
                    }
                }
            }
        }
    }
}