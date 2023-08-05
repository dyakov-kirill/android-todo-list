package com.dyakov.todolist.ui.create

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dyakov.todolist.data.Database
import com.dyakov.todolist.Priority
import com.dyakov.todolist.TodoItem
import com.dyakov.todolist.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateUiState())
    val uiState: StateFlow<CreateUiState> = _uiState

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun updatePriority(priority: Priority) {
        _uiState.value = _uiState.value.copy(priority = priority)
    }

    fun setDeadline(c: Calendar) {
        _uiState.value = _uiState.value.copy(deadline = Date(c.timeInMillis))
        _uiState.value = _uiState.value.copy(isDeadlineSet = true)

    }

    fun removeDeadline() {
        _uiState.value = _uiState.value.copy(deadline = null)
        _uiState.value = _uiState.value.copy(isDeadlineSet = false)
    }

    private fun createItem() = TodoItem(
        System.currentTimeMillis().toString(),
        _uiState.value.description,
        _uiState.value.priority,
        false,
        _uiState.value.deadline,
        Date(System.currentTimeMillis()),
        null
    )

    fun saveTask() = viewModelScope.launch {
        repository.addTask(createItem())
    }
}