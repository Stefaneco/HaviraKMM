package com.piotrkalin.havira.android.group.create.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.piotrkalin.havira.android.R
import com.piotrkalin.havira.android.core.presentation.FilledTitleTextField
import com.piotrkalin.havira.group.presentation.create.CreateGroupEvent
import com.piotrkalin.havira.group.presentation.create.CreateGroupState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen(
    state: CreateGroupState,
    onEvent: (CreateGroupEvent) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = state.error, block = {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            onEvent(CreateGroupEvent.OnErrorSeen)
        }
    })

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.title_create_group)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(CreateGroupEvent.BackButtonPressed) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button_description)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.isCreated) {
            CreatedGroupScreen(state = state, onEvent = onEvent, paddingValues = paddingValues)
        }
        else if (state.isCreating) {
            CreatingGroupScreen(state = state, onEvent = onEvent, paddingValues = paddingValues)
        }
        else {
            BaseCreateGroupScreen(state = state, onEvent = onEvent, paddingValues = paddingValues)
        }
    }
}

@Composable
fun BaseCreateGroupScreen(
    state: CreateGroupState,
    onEvent: (CreateGroupEvent) -> Unit,
    paddingValues: PaddingValues
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        FilledTitleTextField(
            modifier = Modifier.padding(vertical = 16.dp),
            fieldName = stringResource(id = R.string.group_name_hint),
            title = state.groupName,
            editTitle = { onEvent(CreateGroupEvent.EditGroupName(it)) }
        )
        Button(
            onClick =  { onEvent(CreateGroupEvent.CreateGroup) },
            enabled = state.isValidGroup) {
            Text(text = stringResource(id = R.string.create_button_label))
        }
    }
}

@Composable
fun CreatedGroupScreen(
    state: CreateGroupState,
    onEvent: (CreateGroupEvent) -> Unit,
    paddingValues: PaddingValues
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            FilledTitleTextField(
                modifier = Modifier.padding(vertical = 16.dp),
                fieldName = stringResource(id = R.string.group_name_hint),
                title = state.groupName,
                editTitle = { onEvent(CreateGroupEvent.EditGroupName(it)) },
                enabled = false
            )
            JoinCodeDisplay(joinCode = state.joinCode ?: "Join code not loaded")
            /*Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(id = R.string.join_code_x_text, state.joinCode ?: "")
            )*/
        }
        Button(
            onClick =  { onEvent(CreateGroupEvent.NavigateToCreatedGroup) }) {
            Text(text = stringResource(id = R.string.cool_acknowledge_button_label))
        }
    }
}



@Composable
fun CreatingGroupScreen(
    state: CreateGroupState,
    onEvent: (CreateGroupEvent) -> Unit,
    paddingValues: PaddingValues
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center
    ){
        CircularProgressIndicator()
    }
}