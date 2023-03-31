package com.example.offlinefirsttodoapp.domain.models

data class StarredTasksAndTaskLists(
    val starredTasks: List<Task>,
    val taskLists: List<TaskListWithTasks>
)

data class StarredTasksAndSingleTaskList(
    val starredTasks: List<Task>,
    val taskList: TaskListWithTasks
)