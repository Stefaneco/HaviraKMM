package com.piotrkalin.havira.android.group.create.presentation

import androidx.compose.animation.*
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.piotrkalin.havira.android.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinCodeDisplay(
    modifier: Modifier = Modifier,
    joinCode: String
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    var isCopied by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LaunchedEffect(key1 = isCopied, block = {
            if(isCopied){
                delay(3000)
                isCopied = false
            }
        })

        Box(){
            TextField(
                modifier = modifier
                    .fillMaxWidth(),
                value = joinCode,
                shape = MaterialTheme.shapes.large,
                label = {
                    Text(text = stringResource(id = R.string.join_code_hint))
                },
                onValueChange = {},
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.headlineMedium,
                enabled = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.ContentCopy,
                        contentDescription = stringResource(id = R.string.copy_join_code_to_clipboard_button_description))
                }
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .alpha(1f)
                    .border(
                        width = 2.dp,
                        color = Color.Gray,
                        shape = MaterialTheme.shapes.large
                    )
                    .clickable(onClick = {
                        clipboardManager.setText(AnnotatedString((joinCode)))
                        isCopied = true
                    }),
            )
        }
        AnimatedVisibility(
            visible = isCopied,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(id = R.string.copied_to_clipboard)
            )
        }
    }
}