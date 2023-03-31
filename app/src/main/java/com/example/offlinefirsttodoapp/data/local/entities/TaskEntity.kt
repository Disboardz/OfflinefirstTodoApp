package com.example.offlinefirsttodoapp.data.local.entities

import androidx.room.*
import java.util.*

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val taskId: Int = 0,
    val taskListCreatorId: Int,
    val title: String,
    val starred: Boolean = false,
    val details: String? = null,
    val completed: Boolean = false,
    val createdAt: Long = Calendar.getInstance().timeInMillis,
    val deadline: Long? = null
)


