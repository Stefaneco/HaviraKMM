@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.havira.android.dish.presentation.create

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.example.havira.dish.presentation.create.CreateDishEvent
import com.example.havira.dish.presentation.create.CreateDishState


@Composable
fun CreateDishScreen(
    state : CreateDishState,
    onEvent : (CreateDishEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {  },
                navigationIcon = {
                    IconButton(onClick = { onEvent(CreateDishEvent.BackButtonPressed) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior)
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            FilledDishEditSection(
                title = state.title,
                desc = state.desc,
                editDesc = { onEvent(CreateDishEvent.EditDescription(it))},
                editTitle = { onEvent(CreateDishEvent.EditTitle(it))}
            )
            Button(
                onClick =  { onEvent(CreateDishEvent.CreateDish()) },
                enabled = state.isValidDish) {
                Text(text = "Save")
            }
        }
    }
}

@Composable
fun FilledDishEditSection(
    title : String,
    desc : String,
    editTitle: (String) -> Unit,
    editDesc : (String) -> Unit,

){
    var isTitleFocused by remember { mutableStateOf(false) }

    Column() {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isTitleFocused = when {
                        focusState.isFocused -> true
                        else -> false
                    }
                },
            value = title,
            shape = MaterialTheme.shapes.large,
            label = {
                if(title.isNotBlank() || isTitleFocused) Text(text = "Title")
                else Text(text = "Title", style = MaterialTheme.typography.headlineMedium)
            },
            onValueChange = {
                editTitle(it)
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
            value = desc,
            shape = MaterialTheme.shapes.large,
            label = { Text(text = "Description")},
            onValueChange = {
                editDesc(it)
            },
            singleLine = false,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
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