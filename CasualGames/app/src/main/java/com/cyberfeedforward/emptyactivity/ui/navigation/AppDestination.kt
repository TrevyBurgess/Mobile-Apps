package com.cyberfeedforward.emptyactivity.ui.navigation

sealed class AppDestination(
    val route: String,
    val label: String
) {
    data object Home : AppDestination(
        route = "home",
        label = "Home"
    )

    data object Games : AppDestination(
        route = "games",
        label = "Games"
    )

    data object Sudoku : AppDestination(
        route = "games/sudoku",
        label = "Sudoku"
    )

    data object Settings : AppDestination(
        route = "settings",
        label = "Settings"
    )

    companion object {
        val bottomNavDestinations: List<AppDestination> = listOf(Home, Games, Settings)
    }
}
