package com.example.offlinefirsttodoapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.offlinefirsttodoapp.R
import com.example.offlinefirsttodoapp.domain.models.TaskListWithTasks

const val TAG = "TAB_LIST"

@Composable
private fun TabFancyIndicator(
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .height(3.dp)
            .padding(horizontal = 5.dp)
            .border(BorderStroke(2.dp, color), RoundedCornerShape(topEnd = 5.dp, topStart = 5.dp))
    )
}

@Composable
private fun AnimatedTabFancyIndicator(
    tabPositions: List<TabPosition>,
    selectedTabIndex: Int
) {
    val transition = updateTransition(targetState = selectedTabIndex, label = "")
    val indicatorStart by transition.animateDp(
        transitionSpec = {
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 50f)
            } else {
                spring(dampingRatio = 1f, stiffness = 1000f)
            }
        }, label = "indicatorStart"
    ) {
        tabPositions[it].left
    }

    val indicatorEnd by transition.animateDp(
        transitionSpec = {
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 1000f)
            } else {
                spring(dampingRatio = 1f, stiffness = 50f)
            }
        }, label = "indicatorEnd"
    ) {
        tabPositions[it].right
    }

    TabFancyIndicator(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomStart)
            .offset(x = indicatorStart)
            .width(indicatorEnd - indicatorStart)
    )
}

@Composable
fun TabList(
    taskList: List<TaskListWithTasks>,
    selectedTabIndex: Int,
    changePage: (page: Int) -> Unit,
    openCreateList: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val color by animateColorAsState(
        targetValue = if (isPressed) MaterialTheme.colorScheme.primary else Color.Gray,
        animationSpec = tween(100, delayMillis = 0)
    )

    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = MaterialTheme.colorScheme.background,
        edgePadding = 0.dp,
        indicator = { tabPositions ->
            AnimatedTabFancyIndicator(
                tabPositions = tabPositions,
                selectedTabIndex = selectedTabIndex
            )
        },
        divider = {}
    ) {
        taskList.forEachIndexed { i, data ->
            if (data.taskList.title == "starred") {
                Tab(selected = selectedTabIndex == i, onClick = { changePage(i) }, unselectedContentColor = Color.Gray, icon = {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "New list",
                        Modifier.size(20.dp)
                    )
                })
            } else {
                Tab(selected = selectedTabIndex == i, onClick = { changePage(i) }, unselectedContentColor = Color.Gray, text = {
                    Text(text = data.taskList.title)
                })
            }
        }
        LeadingIconTab(
            selected = false,
            onClick = openCreateList,
            unselectedContentColor = color,
            text = { Text(text = stringResource(id = R.string.new_list)) },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "",
                    Modifier.size(20.dp)
                )
            },
            interactionSource = interactionSource
        )
    }
    Divider()
}
