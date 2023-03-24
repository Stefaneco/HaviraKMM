package com.piotrkalin.havira.android.core.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilledTitleTextField(
    modifier: Modifier = Modifier,
    fieldName: String,
    title : String,
    editTitle: (String) -> Unit,
    enabled: Boolean = true
) {
    var isTitleFocused by remember { mutableStateOf(false) }

    TextField(
        modifier = modifier
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
            if(title.isNotBlank() || isTitleFocused) Text(text = fieldName)
            else Text(text = fieldName, style = MaterialTheme.typography.headlineMedium)
        },
        onValueChange = {
            editTitle(it)
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        textStyle = MaterialTheme.typography.headlineMedium,
        enabled = enabled
    )
}