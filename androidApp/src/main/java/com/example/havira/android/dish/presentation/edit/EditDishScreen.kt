package com.example.havira.android.dish.presentation.edit

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.example.havira.android.dish.presentation.create.FilledDishEditSection
import com.example.havira.dish.presentation.edit.DishEditEvent
import com.example.havira.dish.presentation.edit.DishEditState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDishScreen(
    state: DishEditState,
    onEvent: (DishEditEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {  },
                navigationIcon = {
                    IconButton(onClick = { onEvent(DishEditEvent.BackButtonPressed) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onEvent(DishEditEvent.OpenDeleteDialog) }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
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
            Text(text = "Cancel")
        }},
        confirmButton = {TextButton(onClick = { onConfirm() }) {
            Text(text = "Delete")
        }},
        title = { Text(text = "Delete $dishTitle")},
        text = { Text(text = "Are you sure you want to delete this dish? This action can't be reversed!")}
    )
}
