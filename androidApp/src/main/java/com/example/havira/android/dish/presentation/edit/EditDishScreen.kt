package com.example.havira.android.dish.presentation.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.havira.android.dish.presentation.create.FilledDishEditSection
import com.example.havira.dish.presentation.edit.DishEditEvent
import com.example.havira.dish.presentation.edit.DishEditState

@Composable
fun EditDishScreen(
    state: DishEditState,
    onEvent: (DishEditEvent) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp, horizontal = 4.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ){
        FilledDishEditSection(
            title = state.title,
            desc = state.desc,
            editTitle = { onEvent(DishEditEvent.EditTitle(it)) },
            editDesc = { onEvent(DishEditEvent.EditDescription(it)) }

        )

        Button(
            onClick =  { onEvent(DishEditEvent.EditDish()) },
            enabled = state.isValidDish) {
            Text(text = "Save")
        }
    }
}

