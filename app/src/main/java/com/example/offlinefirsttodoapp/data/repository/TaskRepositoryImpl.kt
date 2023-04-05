package com.example.offlinefirsttodoapp.data.repository

import android.util.Log
import com.example.offlinefirsttodoapp.data.local.TodoAppDatabase
import com.example.offlinefirsttodoapp.data.local.entities.TaskEntity
import com.example.offlinefirsttodoapp.data.local.entities.TaskListEntity
import com.example.offlinefirsttodoapp.data.mapper.toTask
import com.example.offlinefirsttodoapp.data.mapper.toTaskEntity
import com.example.offlinefirsttodoapp.data.mapper.toViewData
import com.example.offlinefirsttodoapp.domain.models.StarredTasksAndSingleTaskList
import com.example.offlinefirsttodoapp.domain.models.Task
import com.example.offlinefirsttodoapp.domain.models.TaskListWithTasks
import com.example.offlinefirsttodoapp.domain.repository.TasksRepository
import com.example.offlinefirsttodoapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    db: TodoAppDatabase
) : TasksRepository {
    private val taskDao = db.taskDao()

    // TASK LIST OPERATIONS

    override suspend fun insertTaskList(taskList: TaskListEntity): Flow<Resource<Boolean>> {
        return flow {
            taskDao.insert(taskList)
            emit(Resource.Success(true))
        }
    }

    override suspend fun updateTaskList(taskList: TaskListEntity) = taskDao.update(taskList)

    override suspend fun deleteTaskList(taskList: TaskListEntity): Flow<Resource<Boolean>> {
        return flow {
            taskDao.delete(taskList)
            emit(Resource.Success(true))
        }
    }

    override suspend fun getAllTaskLists(firstLoad: Boolean): Flow<Resource<List<TaskListWithTasks>>> {
        return flow {
            if (!firstLoad) emit(Resource.Loading(true))
            val taskLists = taskDao.getAllTaskLists().map { it.toViewData() }
            val starredTasks = taskDao.getStarredTasks().map { task -> task.toTask() }
            taskLists[0].tasks = starredTasks
            Log.d("task_repository", "${starredTasks.size}, ${taskDao.getStarredTasks().size}")

            emit(Resource.Success( data = taskLists))
            if (!firstLoad) emit(Resource.Loading(false))
        }
    }

    override suspend fun getTaskList(id: Int): StarredTasksAndSingleTaskList {
        val taskList = taskDao.getTaskList(id).toViewData()
        val starredTask = taskDao.getStarredTasks().map { task -> task.toTask() }
        return StarredTasksAndSingleTaskList(starredTask, taskList)
    }

    // TASK OPERATIONS

    override suspend fun insertTask(task: TaskEntity): Flow<Resource<Boolean>> {
        return flow {
            taskDao.insertTask(task)
            emit(Resource.Success(true))
        }
    }

    override suspend fun getTask(id: Int): Flow<Resource<Task>> {
        return flow {
            emit(Resource.Loading(true))
            val task = taskDao.getTask(id).toTask()
            emit(Resource.Success(data = task))
        }
    }

    override suspend fun updateTask(task: Task): Flow<Resource<Boolean>> {
        val taskEntity = task.toTaskEntity()
        return flow {
            taskDao.updateTask(taskEntity)
            emit(Resource.Success(true))
        }
    }

    override suspend fun deleteTask(task: Task): Flow<Resource<Boolean>> {
        return flow {
            taskDao.deleteTask(task.toTaskEntity())
            emit(Resource.Success(true))
        }
    }
}
