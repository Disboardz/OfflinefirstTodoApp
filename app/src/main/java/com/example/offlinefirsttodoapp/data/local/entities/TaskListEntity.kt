package com.example.offlinefirsttodoapp.data.local.entities

import androidx.room.*
import com.example.offlinefirsttodoapp.domain.models.Task
import java.util.*

@Entity(tableName = "task_list")
data class TaskListEntity(
    @PrimaryKey(autoGenerate = true) val taskListId: Int = 0,
    val title: String,
    val canDelete: Boolean = true,
    val createdAt: Long = Calendar.getInstance().timeInMillis
)

data class TaskListWithTasksEntity(
    @Embedded val taskList: TaskListEntity,
    @Relation(
        parentColumn = "taskListId",
        entityColumn = "taskListCreatorId"
    )
    val tasks: List<TaskEntity>
)

data class TaskWithTasksAndSubTasks(
    @Embedded val taskList: TaskListEntity,
    @Relation(
        parentColumn = "taskListId",
        entityColumn = "taskListCreatorId"
    ) val tasks: List<TaskEntity>
)