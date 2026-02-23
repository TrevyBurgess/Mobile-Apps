package com.cyberfeedforward.emptyactivity.ui.state

import androidx.annotation.StringRes
import com.cyberfeedforward.emptyactivity.R

data class SettingsUiState(
    @StringRes val usernameRes: Int = R.string.settings_username,
    val level: Int = 7,
    @StringRes val statusRes: Int = R.string.settings_status
)
