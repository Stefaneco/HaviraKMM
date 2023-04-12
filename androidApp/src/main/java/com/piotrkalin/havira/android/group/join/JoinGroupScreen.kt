package com.piotrkalin.havira.android.group.join

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
import com.piotrkalin.havira.group.presentation.join.JoinGroupEvent
import com.piotrkalin.havira.group.presentation.join.JoinGroupState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinGroupScreen(
    state: JoinGroupState,
    onEvent: (JoinGroupEvent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = state.error, block = {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            onEvent(JoinGroupEvent.OnErrorSeen)
        }
    })

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.title_join_group)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(JoinGroupEvent.BackButtonPressed) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button_description)
                        )
                    }
                }
            )
        }
    ){ paddingValues ->
        if(state.isJoined){
            JoinedGroupScreen(state = state, onEvent = onEvent, paddingValues = paddingValues)
        }
        else if(state.isJoining){
            JoiningGroupScreen(state = state, onEvent = onEvent, paddingValues = paddingValues)
        }
        else {
            BaseJoinGroupScreen(state = state, onEvent = onEvent, paddingValues = paddingValues)
        }
    }
}

@Composable
fun BaseJoinGroupScreen(
    state: JoinGroupState,
    onEvent: (JoinGroupEvent) -> Unit,
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
            fieldName = stringResource(id = R.string.join_code_hint),
            title = state.joinCode,
            editTitle = { onEvent(JoinGroupEvent.EditJoinCode(it)) }
        )
        Button(
            onClick =  { onEvent(JoinGroupEvent.JoinGroup) },
            enabled = state.isValidCode) {
            Text(text = stringResource(id = R.string.join_button_label))
        }
    }
}



@Composable
fun JoinedGroupScreen(
    state: JoinGroupState,
    onEvent: (JoinGroupEvent) -> Unit,
    paddingValues: PaddingValues
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Column {
            FilledTitleTextField(
                modifier = Modifier.padding(vertical = 16.dp),
                fieldName = stringResource(id = R.string.join_code_hint),
                title = state.joinCode,
                editTitle = {  },
                enabled = false
            )
            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(id = R.string.successfully_joined_the_group_text)
            )
        }
        Button(
            onClick =  { onEvent(JoinGroupEvent.NavigateToJoinedGroup) }) {
            Text(text = stringResource(id = R.string.cool_acknowledge_button_label))
        }
    }
}

@Composable
fun JoiningGroupScreen(
    state: JoinGroupState,
    onEvent: (JoinGroupEvent) -> Unit,
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