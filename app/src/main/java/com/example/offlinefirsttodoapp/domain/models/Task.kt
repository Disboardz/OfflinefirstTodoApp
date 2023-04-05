package com.example.offlinefirsttodoapp.domain.models

data class Task(
    val taskId: Int,
    val title: String,
    val starred: Boolean,
    val details: String?,
    val createdAt: Long,
    val completed: Boolean,
    val deadline: Long?,
    val taskListCreatorId: Int
)