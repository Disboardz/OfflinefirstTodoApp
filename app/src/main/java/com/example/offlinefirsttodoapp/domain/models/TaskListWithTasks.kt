package com.example.offlinefirsttodoapp.domain.models

data class TaskListWithTasks(
    val taskList: TaskList,
    var tasks: List<Task>,
    val orderBy: OrderListOption = OrderListOption.MY_ORDER
)
