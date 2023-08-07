package com.dyakov.todolist.ui.list.viewmodel

import androidx.lifecycle.viewModelScope
import com.dyakov.todolist.TodoItem
import com.dyakov.todolist.data.TaskRepository
import com.dyakov.todolist.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for ListFragment
 */
@HiltViewModel
class ListViewModel @Inject constructor(private val repository: TaskRepository) : BaseViewModel<ListUiState>() {

    override val initialState: ListUiState = ListUiState(emptyList(), false)
    private var getTasksJob: Job? = null

    init {
        getTasksJob = viewModelScope.launch {
            repository.getAllTasks().cancellable().collect {
                _uiState.value = _uiState.value.copy(data = it)
            }
        }
    }

    fun changeHideState() {
        getTasksJob?.cancel()
        setState { copy(isDoneHidden = !_uiState.value.isDoneHidden) }
        getTasksJob = viewModelScope.launch {
            when (uiState.value.isDoneHidden) {
                false -> {
                    repository.getAllTasks().cancellable().collect {
                        setState { copy(data = it) }
                    }
                }
                true -> {
                    repository.getDoneTasks().cancellable().collect {
                        setState { copy(data = it) }
                    }
                }
            }
        }
    }

    fun updateTask(item: TodoItem) = viewModelScope.launch {
        repository.updateTask(item)
    }

    fun deleteTask(item: TodoItem) = viewModelScope.launch {
        repository.deleteTask(item)
    }
}
