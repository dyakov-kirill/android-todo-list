package com.dyakov.todolist.ui.edit

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
class EditViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(EditUiState())
    val uiState: StateFlow<EditUiState> = _uiState

    private var isBasicStateSet = false

    fun setBasicState(item: TodoItem) {
        if (!isBasicStateSet) {
            _uiState.value = _uiState.value.copy(
                id = item.id,
                description = item.description,
                priority = item.priority,
                isDone = item.isDone,
                deadline = item.deadline,
                isDeadlineSet = item.deadline != null,
                creationTime = item.creationTime
            )
        }
        isBasicStateSet = true
    }

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
        _uiState.value.id,
        _uiState.value.description,
        _uiState.value.priority,
        _uiState.value.isDone,
        _uiState.value.deadline,
        _uiState.value.creationTime,
        Date(System.currentTimeMillis())
    )

    fun updateTask() = viewModelScope.launch {
        repository.updateTask(createItem())
    }
}