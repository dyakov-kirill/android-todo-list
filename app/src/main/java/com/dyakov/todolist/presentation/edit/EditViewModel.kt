package com.dyakov.todolist.presentation.edit

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
 * ViewModel for EditFragment
 */
@HiltViewModel
class EditViewModel @Inject constructor(private val interactor: TodoItemInteractor) : BaseViewModel<EditUiState>() {
    override val initialState: EditUiState = EditUiState()
    private var isBasicStateSet = false

    fun setBasicState(item: TodoItem) {
        if (!isBasicStateSet) {
            setState {
                copy(
                    id = item.id,
                    description = item.description,
                    priority = item.priority,
                    isDone = item.isDone,
                    deadline = item.deadline,
                    isDeadlineSet = item.deadline != null,
                    creationTime = item.creationTime
                )
            }
        }
        isBasicStateSet = true
    }

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
        uiState.value.id,
        uiState.value.description,
        uiState.value.priority,
        uiState.value.isDone,
        uiState.value.deadline,
        uiState.value.creationTime,
        Date(System.currentTimeMillis())
    )

    fun updateTask() {
        viewModelScope.launch {
            interactor.updateTask(createItem())
        }
    }
}
