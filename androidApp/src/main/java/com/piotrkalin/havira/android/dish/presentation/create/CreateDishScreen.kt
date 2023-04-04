@file:OptIn(ExperimentalMaterial3Api::class)

package com.piotrkalin.havira.android.dish.presentation.create

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.piotrkalin.havira.android.R
import com.piotrkalin.havira.android.core.presentation.FilledTitleTextField
import com.piotrkalin.havira.android.core.presentation.LoadingScreen
import com.piotrkalin.havira.dish.presentation.create.CreateDishEvent
import com.piotrkalin.havira.dish.presentation.create.CreateDishState
import kotlinx.coroutines.launch


@Composable
fun CreateDishScreen(
    state : CreateDishState,
    onEvent : (CreateDishEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {  },
                navigationIcon = {
                    IconButton(onClick = { onEvent(CreateDishEvent.BackButtonPressed) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button_description)
                        )
                    }
                },
                scrollBehavior = scrollBehavior)
        }
    ) { paddingValues ->
        if(state.isCreating) LoadingScreen(Modifier.padding(paddingValues))
        else BaseCreateDishScreen(state = state, onEvent = onEvent, paddingValues = paddingValues)

        state.error?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it)
                onEvent(CreateDishEvent.OnErrorSeen)
            }
        }
    }
}

@Composable
fun BaseCreateDishScreen(
    state : CreateDishState,
    onEvent : (CreateDishEvent) -> Unit,
    paddingValues: PaddingValues
){
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        FilledDishEditSection(
            title = state.title,
            desc = state.desc,
            editDesc = { onEvent(CreateDishEvent.EditDescription(it)) },
            editTitle = { onEvent(CreateDishEvent.EditTitle(it))}
        )
        Button(
            onClick =  { onEvent(CreateDishEvent.CreateDish()) },
            enabled = state.isValidDish) {
            Text(text = stringResource(id = R.string.save_button_label))
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

    Column {
        FilledTitleTextField(fieldName = stringResource(id = R.string.title_dish_title), title = title, editTitle = editTitle)
        Spacer(modifier = Modifier.padding(4.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 200.dp)
                .heightIn(200.dp, 350.dp),
            value = desc,
            shape = MaterialTheme.shapes.large,
            label = { Text(text = stringResource(id = R.string.title_dish_description))},
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