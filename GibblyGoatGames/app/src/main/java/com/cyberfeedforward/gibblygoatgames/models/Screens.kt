package com.cyberfeedforward.gibblygoatgames.models

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.cyberfeedforward.gibblygoatgames.R

sealed class Screens(
    val route: String,
    @StringRes val labelResourceId: Int,
    val icon: ImageVector
) {
    data object Home: Screens("home", R.string.nav_home, Icons.Filled.Home)

    data object Games: Screens("games", R.string.nav_games, Icons.Filled.Star)
}

val screenList = listOf(Screens.Home, Screens.Games)
