package com.example.offlinefirsttodoapp.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.offlinefirsttodoapp.R
import com.example.offlinefirsttodoapp.domain.models.TaskListWithTasks

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu(
    modifier: Modifier = Modifier,
    taskList: List<TaskListWithTasks>,
    page: Int,
    changePage: (page: Int) -> Unit,
    onNavigateToCreateList: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .weight(1f, false)
        ) {
            ListItem(
                headlineText = {
                    Text(
                        text = stringResource(id = R.string.starred),
                        fontWeight = FontWeight.W500
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Sharp.Star,
                        contentDescription = stringResource(id = R.string.starred)
                    )
                },
                modifier = Modifier
                    .clickable(
                        onClick = { changePage(0) },
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    )
                    .padding(top = 5.dp, bottom = 5.dp, end = 10.dp)
                    .clip(RoundedCornerShape(topEnd = 50.dp, bottomEnd = 50.dp)),
                colors = ListItemDefaults.colors(
                    containerColor = if (page == 0) MaterialTheme.colorScheme.primaryContainer else ListItemDefaults.containerColor,
                    headlineColor = if (page == 0) MaterialTheme.colorScheme.primary else ListItemDefaults.contentColor,
                    leadingIconColor = if (page == 0) MaterialTheme.colorScheme.primary else ListItemDefaults.contentColor
                )
            )
            Divider(Modifier.padding(bottom = 10.dp))
            taskList.forEachIndexed { i, item ->
                if (item.taskList.title == "starred") return@forEachIndexed
                ListItem(
                    headlineText = {
                        Text(
                            text = item.taskList.title,
                            modifier = Modifier.padding(start = 35.dp)
                        )
                    },
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clip(RoundedCornerShape(topEnd = 50.dp, bottomEnd = 50.dp))
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null,
                            onClick = {
                                changePage(i)
                            }
                        ),
                    colors = ListItemDefaults.colors(
                        containerColor = if (page == i) MaterialTheme.colorScheme.primaryContainer else ListItemDefaults.containerColor,
                        headlineColor = if (page == i) MaterialTheme.colorScheme.primary else ListItemDefaults.contentColor,
                    )
                )
            }
        }
        Divider(Modifier.padding(vertical = 10.dp))
        ListItem(
            headlineText = {
                Text(
                    text = stringResource(id = R.string.new_list),
                    fontWeight = FontWeight.W500
                )
            },
            leadingContent = {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "")
            },
            modifier = Modifier.clickable(onClick = onNavigateToCreateList)
        )

    }
}