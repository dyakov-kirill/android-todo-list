package com.dyakov.todolist.presentation.list.viewmodel

import androidx.lifecycle.viewModelScope
import com.dyakov.todolist.domain.TodoItemInteractor
import com.dyakov.todolist.domain.models.TodoItem
import com.dyakov.todolist.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for ListFragment
 */
@HiltViewModel
class ListViewModel @Inject constructor(private val interactor: TodoItemInteractor) : BaseViewModel<ListUiState>() {

    override val initialState: ListUiState = ListUiState(emptyList(), false)
    private var getTasksJob: Job? = null

    init {
        getTasksJob = viewModelScope.launch {
            interactor.getAllTasks().cancellable().collect {
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
                    interactor.getAllTasks().cancellable().collect {
                        setState { copy(data = it) }
                    }
                }
                true -> {
                    interactor.getDoneTasks().cancellable().collect {
                        setState { copy(data = it) }
                    }
                }
            }
        }
    }

    fun updateTask(item: TodoItem) {
        viewModelScope.launch {
            interactor.updateTask(item)
        }
    }

    fun deleteTask(item: TodoItem) {
        viewModelScope.launch {
            interactor.deleteTask(item)
        }
    }
}
