package com.dyakov.todolist.presentation.create

import androidx.lifecycle.viewModelScope
import com.dyakov.todolist.domain.TodoItemInteractor
import com.dyakov.todolist.domain.models.Priority
import com.dyakov.todolist.domain.models.TodoItem
import com.dyakov.todolist.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

/**
 * ViewModel for CreateFragment
 */
@HiltViewModel
class CreateViewModel @Inject constructor(private val interactor: TodoItemInteractor) : BaseViewModel<CreateUiState>() {
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

    fun saveTask() {
        viewModelScope.launch {
            interactor.addTask(createItem())
        }
    }
}
