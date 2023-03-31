package com.example.offlinefirsttodoapp.ui.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.offlinefirsttodoapp.R
import kotlin.reflect.KFunction1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskList(
    dismiss: () -> Unit,
    createTaskList: KFunction1<String, Unit>
) {
    var listName by rememberSaveable { mutableStateOf("") }
    val enabled by remember {
        derivedStateOf { listName.isNotEmpty() }
    }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BackHandler(true) {
        dismiss()
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.create_new_list),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = dismiss) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Close create list page"
                            )
                        }
                    },
                    actions = {
                        TextButton(onClick = { createTaskList(listName) }, enabled = enabled) {
                            Text(
                                text = stringResource(id = R.string.done),
                                color = if (enabled) MaterialTheme.colorScheme.primary else Color.Gray,
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(Modifier.padding(innerPadding)) {
                Divider()
                BasicTextField(
                    value = listName,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (enabled) {
                                createTaskList(listName)
                            }
                        }
                    ),
                    maxLines = 1,
                    modifier = Modifier.focusRequester(focusRequester),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    onValueChange = { newText -> listName = newText },
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 25.dp, vertical = 15.dp)
                        ) {
                            if (listName.isEmpty()) {
                                Text(
                                    text = stringResource(id = R.string.create_list_placeholder),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.Gray
                                )
                            }
                            innerTextField()
                        }
                    })
                Divider()
            }

        }
    }

}