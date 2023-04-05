package com.example.offlinefirsttodoapp.ui.screens.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.offlinefirsttodoapp.R
import com.example.offlinefirsttodoapp.domain.models.Task
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1

@Composable
fun TaskItem(
    task: Task,
    onItemClick: (taskId: Int) -> Unit,
    updateTask: (task: Task) -> Unit,
    modifier: Modifier = Modifier
) {
    var completed by remember { mutableStateOf(task.completed) }
    val interactionSource = remember { MutableInteractionSource() }
    val coroutineScope = rememberCoroutineScope()

    fun changeCompleted() {
        completed = !completed
        coroutineScope.launch {
            delay(500)
            updateTask(task.copy(completed = !task.completed))
        }
    }

    Row(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = { onItemClick(task.taskId) }
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = dimensionResource(id = R.dimen.item_horizontal_space),
                    vertical = dimensionResource(id = R.dimen.item_vertical_space)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomCheck(completed = completed, onClick = { changeCompleted() })
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium.copy(textDecoration = if (completed) TextDecoration.LineThrough else null),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 15.dp, end = 15.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (!task.completed) {
                StarredButton(starred = task.starred, onClick = {
                    updateTask(task.copy(starred = !task.starred))
                })
            } else {
                Box(
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .size(24.dp)
                )
            }
        }
    }
}

@Composable
fun CustomCheck(
    completed: Boolean,
    onClick: () -> Unit
) {

    Crossfade(targetState = completed) {
        when (it) {
            true -> {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable(
                            onClick = onClick
                        )
                        .size(24.dp)
                )
            }
            false -> {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_radio_button_unchecked_24),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .clickable(
                            onClick = onClick
                        )
                        .alpha(8f)
                )
            }
        }
    }
}

@Composable
fun StarredButton(
    starred: Boolean,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = if (starred) R.drawable.ic_baseline_star_24 else R.drawable.ic_outline_star_outline_24),
            tint = if (starred) MaterialTheme.colorScheme.primary else Color.Gray,
            contentDescription = null,
            modifier = Modifier.alpha(if (starred) 1f else .8f)
        )
    }
}