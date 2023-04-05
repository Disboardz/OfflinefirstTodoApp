package com.example.offlinefirsttodoapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "subtask")
data class SubTaskEntity(
    @PrimaryKey(autoGenerate = true) val subTaskId: Int = 0,
    val title: String,
    val completed: Boolean,
    val taskCreatorId: Int,
    val details: String? = null,
    val createdAt: Long = Calendar.getInstance().timeInMillis
)
