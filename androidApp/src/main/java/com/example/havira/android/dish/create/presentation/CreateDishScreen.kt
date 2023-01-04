@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.havira.android.dish.create.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.havira.dish.presentation.create.CreateDishEvent
import com.example.havira.dish.presentation.create.CreateDishState


@Composable
fun CreateDishScreen(
    state : CreateDishState,
    onEvent : (CreateDishEvent) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp, horizontal = 4.dp)
    ) {

        FilledCreateDishScreen(state = state, onEvent = onEvent)
        //OutlinedCreateDishScreen(state = state, onEvent = onEvent)

        Button(onClick =  { onEvent(CreateDishEvent.CreateDish()) }) {
            Text(text = "Save")
        }
    }
}

@Composable
fun FilledCreateDishScreen(
    state : CreateDishState,
    onEvent : (CreateDishEvent) -> Unit
){
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.title,
        shape = MaterialTheme.shapes.large,
        label = {
                if(state.title.isNotBlank()) Text(text = "Title")
                else Text(text = "Title", style = MaterialTheme.typography.headlineMedium)
            },
        onValueChange = {
            onEvent(CreateDishEvent.EditTitle(it))
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        textStyle = MaterialTheme.typography.headlineMedium
    )
    Spacer(modifier = Modifier.padding(4.dp))
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 200.dp),
        value = state.desc,
        shape = MaterialTheme.shapes.large,
        label = { Text(text = "Description")},
        onValueChange = {
            onEvent(CreateDishEvent.EditDescription(it))
        },
        singleLine = false,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun OutlinedCreateDishScreen(
    state : CreateDishState,
    onEvent : (CreateDishEvent) -> Unit
){
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.title,
        shape = MaterialTheme.shapes.large,
        label = {
            //if(state.title.isNotBlank()) Text(text = "Title")
            //else
                Text(text = "Title", style = MaterialTheme.typography.headlineMedium)
        },
        onValueChange = {
            onEvent(CreateDishEvent.EditTitle(it))
        },
        textStyle = MaterialTheme.typography.headlineMedium
    )
    Spacer(modifier = Modifier.padding(4.dp))
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 200.dp),
        value = state.desc,
        shape = MaterialTheme.shapes.large,
        label = { Text(text = "Description")},
        onValueChange = {
            onEvent(CreateDishEvent.EditDescription(it))
        },
        singleLine = false)
}