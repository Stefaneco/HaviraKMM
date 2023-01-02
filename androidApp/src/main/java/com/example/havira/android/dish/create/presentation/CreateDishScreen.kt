package com.example.havira.android.dish.create.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import com.example.havira.dish.presentation.create.CreateDishEvent
import com.example.havira.dish.presentation.create.CreateDishState

@Composable
fun CreateDishScreen(
    state : CreateDishState,
    onEvent : (CreateDishEvent) -> Unit
) {
    Column() {
        TextField(
            value = state.title,
            onValueChange = {
            onEvent(CreateDishEvent.EditTitle(it))
        })
        TextField(
            value = state.desc,
            onValueChange = {
                onEvent(CreateDishEvent.EditDescription(it))
            },
            singleLine = false)
        Button(onClick =  { onEvent(CreateDishEvent.CreateDish()) }) {
            Text(text = "Save")
        }
    }
}