package com.example.offlinefirsttodoapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.offlinefirsttodoapp.data.local.entities.TaskEntity
import com.example.offlinefirsttodoapp.data.local.entities.TaskListEntity
import com.example.offlinefirsttodoapp.data.mapper.toTaskListEntity
import com.example.offlinefirsttodoapp.domain.models.Task
import com.example.offlinefirsttodoapp.domain.models.TaskListWithTasks
import com.example.offlinefirsttodoapp.domain.repository.TasksRepository
import com.example.offlinefirsttodoapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TasksRepository
) : ViewModel() {
    private var _state = MutableStateFlow(HomeViewModelState())
    val state = _state.asStateFlow()

    init {
        getTaskList(true, page = 1)
    }

    private fun getTaskList(init: Boolean = false, refreshing: Boolean = false, page: Int? = null) {
        viewModelScope.launch {
            repository.getAllTaskLists(init).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            _state.update { state -> state.copy(taskList = it, currentPage = page ?: state.currentPage) }
                        }
                    }
                    is Resource.Error -> Unit
                    is Resource.Loading -> {
                        if (refreshing) {
                            _state.update { it.copy(refreshing = result.isLoading) }
                        }
                    }
                }
            }
        }
    }

    fun toggleModal() {
        _state.update { state -> state.copy(showModal = !state.showModal) }
    }

    fun changePage(page: Int) {
        _state.update { state -> state.copy(currentPage = page) }
    }

    fun createTaskList(title: String, callback: suspend () -> Unit) {
        val list = TaskListEntity(title = title)
        viewModelScope.launch {
            repository.insertTaskList(list).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        toggleModal()
                        getTaskList(false)
                        delay(400)
                        callback.invoke()
                    }
                    else -> {}
                }
            }
        }
    }

    fun deleteTaskList(list: TaskListWithTasks, callback: suspend () -> Unit) {
        viewModelScope.launch {
            repository.deleteTaskList(list.toTaskListEntity()).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        callback()
                        getTaskList(false)
                    }
                    else -> {}
                }
            }

        }
    }

    private suspend fun getSingleTaskList() {
        val page = _state.value.currentPage
        val id = _state.value.taskList[page].taskList.taskListId
        repository.getTaskList(id).let { result ->
            _state.update { state ->
                val newList = state.taskList.toMutableList()
                newList[page] = result.taskList
                newList[0].tasks = result.starredTasks
                state.copy(taskList = newList.toList())
            }
        }
    }

    fun createTask(task: String, details: String?, deadline: LocalDate?, callback: () -> Unit) {
        val id = _state.value.taskList[_state.value.currentPage].taskList.taskListId
        val newTask = TaskEntity(
            title = task,
            details = details,
            deadline = deadline?.toEpochDay(),
            taskListCreatorId = id
        )
        viewModelScope.launch {
            repository.insertTask(newTask).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        getSingleTaskList()
                        callback.invoke()
                    }
                    else -> {}
                }
            }
        }
    }

    fun updateTask(task: Task, callback: (() -> Unit)? = null) {
        viewModelScope.launch {
            repository.updateTask(task).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        callback?.invoke()
                        getTaskList()
                    }
                    else -> Unit
                }
            }
        }
    }

    fun deleteTask(task: Task, callback: (() -> Unit)? = null) {
        viewModelScope.launch {
            repository.deleteTask(task).collect { result ->
                when(result) {
                    is Resource.Success -> {
                        callback?.invoke()
                        getTaskList()
                    }
                    else -> Unit
                }
            }
        }
    }
}

data class HomeViewModelState(
    val taskList: List<TaskListWithTasks> = emptyList(),
    val refreshing: Boolean = false,
    val error: Boolean = false,
    val showModal: Boolean = false,
    val currentPage: Int = 0
)