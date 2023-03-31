package com.example.offlinefirsttodoapp.domain.models

data class TaskList(
    val taskListId: Int,
    val title: String,
    val canDelete: Boolean = true,
    val createdAt: Long
)
