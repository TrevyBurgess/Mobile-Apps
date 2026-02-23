package com.cyberfeedforward.emptyactivity.ui.state

import androidx.annotation.StringRes
import com.cyberfeedforward.emptyactivity.R

data class GamesUiState(
    @StringRes val titleRes: Int = R.string.games_hub_title,
    val gameCount: Int = 3,
    @StringRes val descriptionRes: Int = R.string.games_description
)
