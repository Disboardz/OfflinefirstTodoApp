package com.example.offlinefirsttodoapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.offlinefirsttodoapp.data.local.daos.TaskDao
import com.example.offlinefirsttodoapp.data.local.entities.*

@Database(entities = [TaskListEntity::class, TaskEntity::class, SubTaskEntity:: class], version = 1, exportSchema = true)
abstract class TodoAppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}