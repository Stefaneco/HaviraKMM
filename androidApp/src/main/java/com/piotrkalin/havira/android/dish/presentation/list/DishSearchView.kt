package com.piotrkalin.havira.android.dish.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.piotrkalin.havira.android.R
import com.piotrkalin.havira.core.domain.model.Dish
import com.piotrkalin.havira.core.domain.util.DateTimeUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishSearchView(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onItemSelected: (Long) -> Unit,
    items: List<Dish>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ){
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = searchText,
                onValueChange = onSearchTextChange,
                leadingIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button_description)
                        )
                    }
                },
                trailingIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(id = R.string.close_search_view_button_description)
                        )
                    }
                },
                placeholder = { Text(
                    text = stringResource(id = R.string.search_dishes_hint),
                )},
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                maxLines = 1
            )
        }
        Divider()
        LazyColumn(){
            items(items) { dish ->
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                DishCard(
                    headline = dish.title,
                    supportingText =  dish.lastMade?.let { DateTimeUtil.formatDate(it) }
                        ?: stringResource(id = R.string.last_made_new_dish),
                    supportingText2 = stringResource(id = R.string.x_ratings, dish.nofRatings),
                    trailingSupportingText = "%.1f".format(dish.rating),
                    modifier = Modifier.clickable { onItemSelected(dish.id!!) })
            }
        }
    }
}