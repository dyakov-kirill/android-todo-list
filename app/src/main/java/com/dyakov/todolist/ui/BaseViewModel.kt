package com.dyakov.todolist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S> : ViewModel() {
    abstract val initialState: S

    protected val _uiState by lazy { MutableStateFlow(initialState) }
    val uiState: StateFlow<S>
        get() = _uiState.asStateFlow()

    protected fun setState(block: S.() -> S) = viewModelScope.launch {
        val currentState = uiState.value
        _uiState.emit(block(currentState))
    }
}
