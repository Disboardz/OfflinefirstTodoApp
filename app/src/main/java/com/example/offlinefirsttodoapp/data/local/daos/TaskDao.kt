package com.example.offlinefirsttodoapp.data.local.daos

import androidx.room.*
import com.example.offlinefirsttodoapp.data.local.entities.TaskEntity
import com.example.offlinefirsttodoapp.data.local.entities.TaskListEntity
import com.example.offlinefirsttodoapp.data.local.entities.TaskListWithTasksEntity

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insert(taskList: TaskListEntity)

    @Update
    suspend fun update(taskList: TaskListEntity)

    @Delete
    suspend fun delete(taskList: TaskListEntity)

    @Transaction
    @Query("select * from task_list")
    suspend fun getAllTaskLists(): List<TaskListWithTasksEntity>

    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insertTask(taskEntity: TaskEntity)

    @Transaction
    @Query("select * from task_list where taskListId = :id")
    suspend fun getTaskList(id: Int): TaskListWithTasksEntity

    @Query("select * from task where taskId = :id")
    suspend fun getTask(id: Int): TaskEntity

    @Query("select * from task where starred = true")
    suspend fun getStarredTasks(): List<TaskEntity>

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)
}