package com.example.offlinefirsttodoapp.data.mapper

import com.example.offlinefirsttodoapp.data.local.entities.TaskEntity
import com.example.offlinefirsttodoapp.data.local.entities.TaskListEntity
import com.example.offlinefirsttodoapp.data.local.entities.TaskListWithTasksEntity
import com.example.offlinefirsttodoapp.domain.models.Task
import com.example.offlinefirsttodoapp.domain.models.TaskList
import com.example.offlinefirsttodoapp.domain.models.TaskListWithTasks

fun TaskListEntity.toTaskList(): TaskList {
    return TaskList(
        taskListId = taskListId,
        title = title,
        canDelete = canDelete,
        createdAt = createdAt
    )
}

fun TaskList.toTaskListEntity(): TaskListEntity {
    return TaskListEntity(
        taskListId = taskListId,
        title = title,
        canDelete = canDelete,
        createdAt = createdAt
    )
}

fun TaskListWithTasks.toTaskListEntity(): TaskListEntity {
    return taskList.toTaskListEntity()
}

fun TaskListWithTasksEntity.toViewData(): TaskListWithTasks {
    return TaskListWithTasks(
        taskList = taskList.toTaskList(),
        tasks = tasks.map { it.toTask() }.toMutableList()
    )
}

fun TaskEntity.toTask(): Task {
    return Task(
        taskId = taskId,
        title = title,
        starred = starred,
        details = details,
        completed = completed,
        createdAt = createdAt,
        deadline = deadline,
        taskListCreatorId = taskListCreatorId
    )
}

fun Task.toTaskEntity(): TaskEntity {
    return TaskEntity(
        taskId = taskId,
        title = title,
        starred = starred,
        details = details,
        completed = completed,
        createdAt = createdAt,
        deadline = deadline,
        taskListCreatorId = taskListCreatorId
    )
}