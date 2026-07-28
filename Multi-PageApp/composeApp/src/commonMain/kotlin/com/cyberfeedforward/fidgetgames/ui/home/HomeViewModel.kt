package com.cyberfeedforward.fidgetgames.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onIncrementCount() {
        _uiState.value = _uiState.value.copy(count = _uiState.value.count + 1)
    }
}

data class HomeUiState(
    val title: String = "Home Page",
    val count: Int = 0
)
