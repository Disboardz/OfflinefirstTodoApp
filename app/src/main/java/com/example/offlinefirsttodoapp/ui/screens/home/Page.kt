package com.example.offlinefirsttodoapp.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.offlinefirsttodoapp.R
import com.example.offlinefirsttodoapp.domain.models.Task
import kotlin.reflect.KFunction1

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Page(
    data: List<Task>,
    navigateToTaskDetails: (taskId: Int) -> Unit,
    updateTask: (task: Task) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    var completedTaskExpanded by rememberSaveable { mutableStateOf(false) }
    val completedTasks = data.filter { task -> task.completed }
    val uncompletedTasks = data.filterNot { task -> task.completed }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 5.dp)
    ) {
        if (data.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .fillParentMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.empty_task_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.padding(top = 5.dp))
                    Text(
                        text = stringResource(id = R.string.empty_task_desc),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 50.dp)
                    )
                }
            }
        }
        itemsIndexed(uncompletedTasks, key = { _, task -> task.taskId }) { _, task ->
            TaskItem(
                task = task,
                onItemClick = navigateToTaskDetails,
                updateTask = { task -> updateTask(task)},
                modifier = Modifier.animateItemPlacement()
            )
        }
        if (completedTasks.isNotEmpty()) {
            item {
                Divider()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = interactionSource,
                                indication = LocalIndication.current
                            ) { completedTaskExpanded = !completedTaskExpanded }
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = dimensionResource(id = R.dimen.item_horizontal_space),
                                    vertical = dimensionResource(id = R.dimen.item_vertical_space)
                                )
                        ) {
                            Text(
                                text = "Completed (${completedTasks.size})",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(start = 5.dp)
                            )
                            Icon(
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = null,
                                Modifier
                                    .minimumInteractiveComponentSize()
                                    .size(24.dp)
                            )
                        }
                    }
                }
            }
            if (completedTaskExpanded) {
                itemsIndexed(completedTasks, key = { _, task -> task.taskId }) { _, task ->
                    TaskItem(
                        task = task,
                        onItemClick = navigateToTaskDetails,
                        updateTask = updateTask,
                        modifier = Modifier.animateItemPlacement()
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.size(40.dp))
            }
        }
    }
}

