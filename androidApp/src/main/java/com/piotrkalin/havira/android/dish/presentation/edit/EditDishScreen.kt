package com.piotrkalin.havira.android.dish.presentation.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.piotrkalin.havira.android.R
import com.piotrkalin.havira.android.dish.presentation.create.FilledDishEditSection
import com.piotrkalin.havira.dish.presentation.edit.DishEditEvent
import com.piotrkalin.havira.dish.presentation.edit.DishEditState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDishScreen(
    state: DishEditState,
    onEvent: (DishEditEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = state.error, block = {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            onEvent(DishEditEvent.OnErrorSeen)
        }
    })

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {  },
                navigationIcon = {
                    IconButton(onClick = { onEvent(DishEditEvent.BackButtonPressed) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button_description)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onEvent(DishEditEvent.OpenDeleteDialog) }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(id = R.string.delete_dish_button_description)
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
                .padding(paddingValues)
                .verticalScroll(scrollState),
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
                Text(text = stringResource(id = R.string.save_button_label))
            }
        }

        if(state.isDeleteDialogOpen){
            DeleteDialog(
                onDismiss = { onEvent(DishEditEvent.DismissDeleteDialog) },
                onConfirm = { onEvent(DishEditEvent.DeleteDish()) },
                dishTitle = state.title
            )
        }
    }
}

@Composable
fun DeleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    dishTitle: String
){
    AlertDialog(
        onDismissRequest = { onDismiss() },
        dismissButton = { TextButton(onClick = { onDismiss() }) {
            Text(text = stringResource(id = R.string.cancel_button_label))
        }},
        confirmButton = {TextButton(onClick = { onConfirm() }) {
            Text(text = stringResource(id = R.string.delete_button_label))
        }},
        title = { Text(text = stringResource(id = R.string.delete_dialog_title, dishTitle))},
        text = { Text(text = stringResource(id = R.string.delete_dish_dialog_message))}
    )
}
