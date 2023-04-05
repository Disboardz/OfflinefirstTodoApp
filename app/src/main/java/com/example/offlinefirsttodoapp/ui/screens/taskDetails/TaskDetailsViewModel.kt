package com.example.offlinefirsttodoapp.ui.screens.taskDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.offlinefirsttodoapp.domain.models.Task
import com.example.offlinefirsttodoapp.domain.repository.TasksRepository
import com.example.offlinefirsttodoapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    handle: SavedStateHandle,
    private val repository: TasksRepository
) : ViewModel() {
    private var _state = MutableStateFlow(TaskDetailsViewModelState())
    private val taskId: Int = checkNotNull(handle["taskId"])

    val state = _state.asStateFlow()

    init {
        getTask()
    }

    fun getTask() {
        viewModelScope.launch {
            repository.getTask(taskId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { state -> state.copy(isLoading = result.isLoading) }
                    }
                    is Resource.Success -> {
                        result.data?.let { task ->
                            _state.update { state ->
                                state.copy(
                                    task = task,
                                    isLoading = false,
                                    date = task.deadline?.let { LocalDate.ofEpochDay(it) },
                                    details = task.details ?: ""
                                )
                            }
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    fun toggleStarred() {
        _state.update { state ->
            val taskUpdated = state.task.copy(starred = !state.task.starred)
            state.copy(task = taskUpdated)
        }
    }

    fun onDetailsChange(newDetails: String) {
        _state.update { state -> state.copy(details = newDetails) }
    }

    fun updateDate(date: LocalDate?) {
        val epoch = date?.toEpochDay()
        _state.update { state ->
            state.copy(
                date = date,
                task = state.task.copy(deadline = epoch)
            )
        }
    }
}

data class TaskDetailsViewModelState(
    val task: Task = Task(
        taskId = -1,
        title = "",
        starred = false,
        details = null,
        createdAt = -1,
        completed = false,
        deadline = null,
        taskListCreatorId = -1
    ),
    val isLoading: Boolean = false,
    val error: Boolean = false,
    val date: LocalDate? = null,
    val details: String = ""
)