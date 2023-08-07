package com.dyakov.todolist.ui.edit

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
 * ViewModel for EditFragment
 */
@HiltViewModel
class EditViewModel @Inject constructor(private val repository: TaskRepository) : BaseViewModel<EditUiState>() {
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

    fun updateTask() = viewModelScope.launch {
        repository.updateTask(createItem())
    }
}
