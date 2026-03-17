package com.cyberfeedforward.emptyactivity.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cyberfeedforward.emptyactivity.ui.state.MahjongUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MahjongViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MahjongUiState())
    val uiState: StateFlow<MahjongUiState> = _uiState.asStateFlow()
}
