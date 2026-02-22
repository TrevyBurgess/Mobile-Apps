package com.cyberfeedforward.emptyactivity.ui.state

data class SettingsUiState(
    val username: String = "PlayerOne",
    val level: Int = 7,
    val status: String = "Ready to play"
)
