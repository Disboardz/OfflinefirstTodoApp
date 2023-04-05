package com.example.offlinefirsttodoapp.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.offlinefirsttodoapp.R
import com.example.offlinefirsttodoapp.domain.models.OrderListOption
import com.example.offlinefirsttodoapp.domain.models.TaskListWithTasks


@Composable
fun ListOptions(
    modifier: Modifier = Modifier,
    taskList: List<TaskListWithTasks>,
    page: Int,
    deleteList: () -> Unit
) {
    val list: TaskListWithTasks = taskList[if (taskList.size == page) page - 1 else page]

    val supportText = when (list.orderBy) {
        OrderListOption.MY_ORDER -> stringResource(id = R.string.sort_by)
        OrderListOption.DATE -> stringResource(id = R.string.sort_by_date)
        OrderListOption.STARRED -> stringResource(id = R.string.sort_by_starred)
    }

    val deleteEnabled = list.taskList.canDelete
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .navigationBarsPadding()
    ) {
        ListItem(
            headlineContent = { Text(text = stringResource(id = R.string.sort_by)) },
            supportingContent = { Text(text = supportText) },
            modifier = Modifier
                .padding(start = 5.dp)
                .clickable(onClick = {})
        )
        Divider()
        Column(modifier = Modifier.padding(start = 5.dp)) {
            ListItem(
                headlineContent = { Text(text = stringResource(id = R.string.rename_list)) },
                modifier = Modifier.clickable(onClick = {})
            )
            ListItem(
                headlineContent = { Text(text = stringResource(id = R.string.delete_list)) },
                modifier = Modifier.clickable(
                    onClick = deleteList,
                    enabled = deleteEnabled
                ),
                colors = androidx.compose.material3.ListItemDefaults.colors(headlineColor = if (deleteEnabled) MaterialTheme.colorScheme.onBackground else Color.Gray)
            )
            ListItem(
                headlineContent = { Text(text = stringResource(id = R.string.delete_completed_task)) },
                modifier = Modifier.clickable(onClick = {})
            )
        }
    }
}
