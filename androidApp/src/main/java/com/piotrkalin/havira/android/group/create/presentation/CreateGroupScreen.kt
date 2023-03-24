package com.piotrkalin.havira.android.group.create.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.piotrkalin.havira.android.core.presentation.FilledTitleTextField
import com.piotrkalin.havira.group.presentation.create.CreateGroupEvent
import com.piotrkalin.havira.group.presentation.create.CreateGroupState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen(
    state: CreateGroupState,
    onEvent: (CreateGroupEvent) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = "Create Group") },
                navigationIcon = {
                    IconButton(onClick = { onEvent(CreateGroupEvent.BackButtonPressed) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
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

        state.error?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it)
                onEvent(CreateGroupEvent.OnErrorSeen)
            }
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
            fieldName = "Group Name",
            title = state.groupName,
            editTitle = { onEvent(CreateGroupEvent.EditGroupName(it)) }
        )
        Button(
            onClick =  { onEvent(CreateGroupEvent.CreateGroup) },
            enabled = state.isValidGroup) {
            Text(text = "Create")
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
                fieldName = "Group Name",
                title = state.groupName,
                editTitle = { onEvent(CreateGroupEvent.EditGroupName(it)) },
                enabled = false
            )
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Code to join: ${state.joinCode}"
            )
        }
        Button(
            onClick =  { onEvent(CreateGroupEvent.NavigateToCreatedGroup) }) {
            Text(text = "Cool!")
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