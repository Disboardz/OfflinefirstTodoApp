package com.example.offlinefirsttodoapp.domain.models

data class SubTask(
    val title: String,
    val details: String? = null,
    val completed: Boolean = false,
    val timeStamp: String? = null
)
