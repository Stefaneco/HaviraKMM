package com.example.havira.android.dish.list.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.example.havira.core.domain.util.DateTimeUtil
import com.example.havira.dish.presentation.list.DishListEvent
import com.example.havira.dish.presentation.list.DishListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishListScreen(
    state: DishListState,
    onEvent : (DishListEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    //TextField(value = "", onValueChange = {}, label = { Text(text = "Search")})
                    BasicTextField(value = "", onValueChange = {})
            },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Localized description"
                    )
                },
                actions = {
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Filled.FilterAlt,
                            contentDescription = "Localized description"
                        )
                    }
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Filled.SwapVert,
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
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(vertical = 4.dp, horizontal = 4.dp)
            ){
                items(state.dishes) { dish ->
                    DishCard(
                        headline = dish.title,
                        supportingText =  dish.lastMade?.let { DateTimeUtil.formatDate(it) } ?: "New dish",
                        supportingText2 = "${dish.nofRatings} ratings",
                        trailingSupportingText = "%.1f".format(dish.rating),
                        modifier = Modifier.clickable { onEvent(DishListEvent.SelectDish(dish.id!!)) })
                }
            }
            Button(onClick = {onEvent(DishListEvent.CreateDish)} ) {
                Text("New Dish")
            }
        }
    }


}