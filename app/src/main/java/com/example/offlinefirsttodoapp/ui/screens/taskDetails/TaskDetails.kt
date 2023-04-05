package com.example.offlinefirsttodoapp.ui.screens.taskDetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.offlinefirsttodoapp.R
import com.example.offlinefirsttodoapp.domain.models.Task
import com.example.offlinefirsttodoapp.ui.components.DateChip
import com.example.offlinefirsttodoapp.ui.theme.LocalSpacing
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarTimeline
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetails(
    viewModel: TaskDetailsViewModel = hiltViewModel(),
    deleteTask: (task: Task, callback: () -> Unit) -> Unit,
    updateTask: (task: Task, callback: () -> Unit) -> Unit,
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val task = state.task
    val calendarState = rememberSheetState()
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    fun checkAndUpdateDetails() {
        if (state.details != state.task.details) {
            updateTask(state.task.copy(details = state.details)) {}
        }
    }

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _: LifecycleOwner, event: Lifecycle.Event ->
            when (event) {
                Lifecycle.Event.ON_DESTROY -> {
                    checkAndUpdateDetails()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    CalendarDialog(state = calendarState, config = CalendarConfig(
        disabledTimeline = CalendarTimeline.PAST
    ), selection = CalendarSelection.Date { date ->
        updateTask(task.copy(deadline = date.toEpochDay())) {
            viewModel.updateDate(date)
        }
    })

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TaskDetailsTopBar(
                navigateBack = navigateBack,
                starred = state.task.starred,
                update = {
                    updateTask(state.task.copy(starred = !state.task.starred)) {
                        viewModel.toggleStarred()
                    }
                },
                delete = { deleteTask(state.task) { navigateBack() } }
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .height(80.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    TextButton(
                        onClick = { updateTask(state.task.copy(completed = true)) { navigateBack() } },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = LocalSpacing.current.medium)
                    ) {
                        Text(text = "Mark completed", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
        ) {
            Text(
                text = state.task.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(
                    top = LocalSpacing.current.small,
                    bottom = LocalSpacing.current.large,
                    start = LocalSpacing.current.medium
                )
            )
            TaskDetailsItem(
                modifier = Modifier.padding(top = LocalSpacing.current.extraSmall),
                icon = R.drawable.ic_baseline_notes_24,
                verticalAlignment = Alignment.Top
            ) {
                BasicTextField(
                    value = state.task.details ?: "",
                    modifier = Modifier.focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    onValueChange = viewModel::onDetailsChange,
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = LocalSpacing.current.medium
                                )
                        ) {
                            if (state.details.isEmpty()) {
                                Text(
                                    text = stringResource(id = R.string.details),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.Gray
                                )
                            }
                            innerTextField()
                        }
                    })
            }
            TaskDetailsItem(
                icon = R.drawable.ic_baseline_access_time_24,
                modifier = Modifier.clickable(enabled = state.date == null) {
                    calendarState.show()
                }
            ) {
                if (state.date != null) {
                    DateChip(
                        modifier = Modifier.padding(start = LocalSpacing.current.medium),
                        text = state.date!!.format(DateTimeFormatter.ofPattern("EE, MMM d")),
                        onTrailingIconClick = {
                            updateTask(state.task.copy(deadline = null)) {
                                viewModel.updateDate(null)
                            }
                        }
                    )
                } else {
                    Text(
                        text = "Add date",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = LocalSpacing.current.medium)
                    )
                }
            }
        }
    }
}



