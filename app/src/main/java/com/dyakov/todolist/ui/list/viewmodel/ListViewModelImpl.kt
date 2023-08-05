package com.dyakov.todolist.ui.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dyakov.todolist.TodoItem
import com.dyakov.todolist.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModelImpl @Inject constructor(private val repository: TaskRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUiState(emptyList(), false))
    val uiState: StateFlow<ListUiState> = _uiState

    fun getTasks() {
        viewModelScope.launch {
            when(_uiState.value.isDoneHidden) {
                false -> {
                    repository.getAllTasks().collect {
                        _uiState.value = _uiState.value.copy(data = it)
                    }
                }
                true -> {
                    repository.getDoneTasks().collect {
                        _uiState.value = _uiState.value.copy(data = it)
                    }
                }
            }
        }
    }

    fun changeHideState() {
        _uiState.value = _uiState.value.copy(isDoneHidden = !_uiState.value.isDoneHidden)
        getTasks()
    }

    fun updateTask(item: TodoItem) = viewModelScope.launch {
        repository.updateTask(item)
    }

    fun deleteTask(item: TodoItem) = viewModelScope.launch {
        repository.deleteTask(item)
    }
}