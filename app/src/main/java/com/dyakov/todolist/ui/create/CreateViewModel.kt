package com.dyakov.todolist.ui.create

import androidx.lifecycle.viewModelScope
import com.dyakov.todolist.Priority
import com.dyakov.todolist.TodoItem
import com.dyakov.todolist.data.TaskRepository
import com.dyakov.todolist.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * ViewModel for CreateFragment
 */
@HiltViewModel
class CreateViewModel @Inject constructor(private val repository: TaskRepository) : BaseViewModel<CreateUiState>() {
    override val initialState: CreateUiState = CreateUiState()

    fun updateDescription(description: String) {
        setState { copy(description = description) }
    }

    fun updatePriority(priority: Priority) {
        setState { copy(priority = priority) }
    }

    fun setDeadline(c: Calendar) {
        setState { copy(deadline = Date(c.timeInMillis), isDeadlineSet = true) }
    }

    fun removeDeadline() {
        setState { copy(deadline = null, isDeadlineSet = false) }
    }

    private fun createItem() = TodoItem(
        System.currentTimeMillis().toString(),
        uiState.value.description,
        uiState.value.priority,
        false,
        uiState.value.deadline,
        Date(System.currentTimeMillis()),
        null
    )

    fun saveTask() = viewModelScope.launch {
        repository.addTask(createItem())
    }
}
