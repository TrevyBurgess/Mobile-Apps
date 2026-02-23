package com.cyberfeedforward.emptyactivity.ui.state

import androidx.annotation.StringRes
import com.cyberfeedforward.emptyactivity.R

data class HomeUiState(
    @StringRes val titleRes: Int = R.string.home_title,
    @StringRes val messageRes: Int = R.string.home_message
)
