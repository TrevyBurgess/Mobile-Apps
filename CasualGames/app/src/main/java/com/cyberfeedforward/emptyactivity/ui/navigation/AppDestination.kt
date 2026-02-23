package com.cyberfeedforward.emptyactivity.ui.navigation

import androidx.annotation.StringRes
import com.cyberfeedforward.emptyactivity.R

sealed class AppDestination(
    val route: String,
    @StringRes val labelRes: Int
) {
    data object Home : AppDestination(
        route = "home",
        labelRes = R.string.nav_home
    )

    data object Games : AppDestination(
        route = "games",
        labelRes = R.string.nav_games
    )

    data object Sudoku : AppDestination(
        route = "games/sudoku",
        labelRes = R.string.sudoku_title
    )

    data object Settings : AppDestination(
        route = "settings",
        labelRes = R.string.nav_settings
    )

    companion object {
        val bottomNavDestinations: List<AppDestination> = listOf(Home, Games, Settings)
    }
}
