package com.example.offlinefirsttodoapp.ui.screens.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.offlinefirsttodoapp.R
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarTimeline
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.reflect.KFunction4

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskSheet(
    createTask: KFunction4<String, String?, LocalDate?, () -> Unit, Unit>,
    focusRequester: FocusRequester,
    toggleSheet: () -> Unit
) {
    var task by remember { mutableStateOf(TextFieldValue(text = "")) }
    var taskDetails by remember { mutableStateOf(TextFieldValue(text = "")) }
    var showDetails by rememberSaveable { mutableStateOf(false) }
    val enabled by remember {
        derivedStateOf { task.text.isNotEmpty() }
    }
    val focusManager = LocalFocusManager.current
    val calendarState = rememberSheetState()
    var selectedDate by rememberSaveable { mutableStateOf<LocalDate?>(null) }


    CalendarDialog(state = calendarState, config = CalendarConfig(
        disabledTimeline = CalendarTimeline.PAST
    ), selection = CalendarSelection.Date { date ->
        selectedDate = date
    })

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .navigationBarsPadding()
            .imePadding()
            .animateContentSize()
    ) {
        BasicTextField(
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            value = task,
            maxLines = 1,
            modifier = Modifier.focusRequester(focusRequester),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            onValueChange = { newText -> task = newText },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp, start = 15.dp)
                ) {
                    if (task.text.isEmpty()) {
                        androidx.compose.material.Text(
                            text = stringResource(id = R.string.new_task),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                }
            })
        if (showDetails) {
            BasicTextField(
                value = taskDetails,
                maxLines = 1,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                onValueChange = { newText -> taskDetails = newText },
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp, start = 25.dp)
                    ) {
                        if (taskDetails.text.isEmpty()) {
                            androidx.compose.material.Text(
                                text = stringResource(id = R.string.details),
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                })
        }
        if (selectedDate != null) {
            FilterChip(selected = false, onClick = {}, label = {
                Text(
                    selectedDate!!.format(
                        DateTimeFormatter.ofPattern("EE, MMM d")
                    )
                )
            }, modifier = Modifier.padding(start = 10.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                })
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row {
                IconButton(onClick =
                { showDetails = true }) {
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
                IconButton(onClick = { calendarState.show() }) {
                    Icon(
                        imageVector = Icons.Rounded.DateRange,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            TextButton(onClick = {
                focusManager.clearFocus()
                createTask(task.text, taskDetails.text, selectedDate) {
                    toggleSheet()
                }
            }, enabled = enabled) {
                Text(
                    text = stringResource(id = R.string.save),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (enabled) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
        }
    }
}