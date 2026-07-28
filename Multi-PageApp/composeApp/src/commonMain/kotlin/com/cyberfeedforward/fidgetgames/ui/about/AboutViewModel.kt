package com.cyberfeedforward.fidgetgames.ui.about

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AboutViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AboutUiState())
    val uiState: StateFlow<AboutUiState> = _uiState.asStateFlow()
}

data class AboutUiState(
    val title: String = "About Page",
    val description: String = "This is a Kotlin Multiplatform app using Jetpack Compose and MVVM.",
    val version: String = "1.0.0"
)
