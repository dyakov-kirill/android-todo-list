package com.dyakov.todolist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S> : ViewModel() {

    private val _state by lazy { MutableStateFlow(initialState) }
    val state: StateFlow<S>
        get() = _state.asStateFlow()


    protected fun setState(block: S.() -> S) = viewModelScope.launch {
        val currentState = state.value
        _state.emit(block(currentState))
    }

    abstract val initialState: S
}