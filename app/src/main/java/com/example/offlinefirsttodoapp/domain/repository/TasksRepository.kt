package com.example.offlinefirsttodoapp.domain.repository

import com.example.offlinefirsttodoapp.data.local.entities.TaskEntity
import com.example.offlinefirsttodoapp.data.local.entities.TaskListEntity
import com.example.offlinefirsttodoapp.domain.models.StarredTasksAndSingleTaskList
import com.example.offlinefirsttodoapp.domain.models.StarredTasksAndTaskLists
import com.example.offlinefirsttodoapp.domain.models.Task
import com.example.offlinefirsttodoapp.domain.models.TaskListWithTasks
import com.example.offlinefirsttodoapp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    suspend fun insertTaskList(taskList: TaskListEntity): Flow<Resource<Boolean>>

    suspend fun updateTaskList(taskList: TaskListEntity)

    suspend fun deleteTaskList(taskList: TaskListEntity): Flow<Resource<Boolean>>

    suspend fun getAllTaskLists(firstLoad: Boolean = false): Flow<Resource<List<TaskListWithTasks>>>

    suspend fun insertTask(task: TaskEntity): Flow<Resource<Boolean>>

    suspend fun getTaskList(id: Int): StarredTasksAndSingleTaskList

    suspend fun getTask(id: Int): Flow<Resource<Task>>

    suspend fun updateTask(task: Task): Flow<Resource<Boolean>>

    suspend fun deleteTask(task: Task): Flow<Resource<Boolean>>
}