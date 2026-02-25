package com.cyberfeedforward.emptyactivity.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cyberfeedforward.emptyactivity.ui.gamepages.GamesHubPage
import com.cyberfeedforward.emptyactivity.ui.gamepages.MiniSudokuPage
import com.cyberfeedforward.emptyactivity.ui.gamepages.SudokuHelpPage
import com.cyberfeedforward.emptyactivity.ui.gamepages.SudokuPage
import com.cyberfeedforward.emptyactivity.ui.navigation.AppDestination
import com.cyberfeedforward.emptyactivity.ui.screens.AboutScreen
import com.cyberfeedforward.emptyactivity.ui.screens.HomeScreen
import com.cyberfeedforward.emptyactivity.ui.screens.SettingsScreen
import com.cyberfeedforward.emptyactivity.ui.theme.EmptyActivityTheme
import com.cyberfeedforward.emptyactivity.ui.viewmodel.HomeViewModel
import com.cyberfeedforward.emptyactivity.ui.viewmodel.MiniSudokuViewModel
import com.cyberfeedforward.emptyactivity.ui.viewmodel.SettingsViewModel
import com.cyberfeedforward.emptyactivity.ui.viewmodel.SudokuViewModel

@Composable
fun AppRoot(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                AppDestination.bottomNavDestinations.forEach { destination ->
                    val selected = currentDestination
                        ?.hierarchy
                        ?.any { it.route == destination.route } == true

                    NavigationBarItem(

                        selected = selected,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = { Text(text = stringResource(destination.labelRes)) },
                        icon = {
                            when (destination) {
                                AppDestination.Home -> Icon(
                                    imageVector = Icons.Outlined.Home,
                                    contentDescription = stringResource(destination.labelRes)
                                )

                                AppDestination.Games -> Icon(
                                    imageVector = Icons.Outlined.SportsEsports,
                                    contentDescription = stringResource(destination.labelRes)
                                )

                                AppDestination.Settings -> Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = stringResource(destination.labelRes)
                                )

                                AppDestination.About -> Icon(
                                    imageVector = Icons.Outlined.Info,
                                    contentDescription = stringResource(destination.labelRes)
                                )

                                else -> {}
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            paddingValues = innerPadding
        )
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Games.route,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        composable(AppDestination.Home.route) {
            HomeRoute()
        }
        composable(AppDestination.Games.route) {
            GamesRoute(
                onOpenSudoku = {
                    navController.navigate(AppDestination.Sudoku.route)
                },
                onOpenMiniSudoku = {
                    navController.navigate(AppDestination.MiniSudoku.route)
                }
            )
        }
        composable(AppDestination.Sudoku.route) {
            SudokuRoute(
                onHelpClick = {
                    navController.navigate(AppDestination.SudokuHelp.route)
                },
                onBackToGames = {
                navController.popBackStack()
            })
        }
        composable(AppDestination.SudokuHelp.route) {
            SudokuHelpRoute(onReturnToSudoku = {
                navController.popBackStack()
            })
        }
        composable(AppDestination.MiniSudoku.route) {
            MiniSudokuRoute(onBackToGames = {
                navController.popBackStack()
            })
        }
        composable(AppDestination.Settings.route) {
            SettingsRoute()
        }
        composable(AppDestination.About.route) {
            AboutRoute()
        }
    }
}

@Composable
private fun HomeRoute(viewModel: HomeViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        uiState = uiState
    )
}

@Composable
private fun GamesRoute(
    onOpenSudoku: () -> Unit,
    onOpenMiniSudoku: () -> Unit
) {
    GamesHubPage(
        onSudokuClick = onOpenSudoku,
        onMiniSudokuClick = onOpenMiniSudoku,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
private fun MiniSudokuRoute(
    onBackToGames: () -> Unit,
    viewModel: MiniSudokuViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MiniSudokuPage(
        board = uiState.board,
        givenCells = uiState.givenCells,
        selectedIndex = uiState.selectedIndex,
        isComplete = uiState.isComplete,
        difficulty = uiState.difficulty,
        onCellSelected = viewModel::selectCell,
        onNumberInput = viewModel::inputNumber,
        onUndoMove = viewModel::undoLastMove,
        onToggleDifficulty = viewModel::toggleDifficulty,
        onBackToGames = onBackToGames,
        onNewGame = viewModel::startNewGame,
        onRestartGame = viewModel::restartCurrentGame,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
private fun SudokuRoute(
    onHelpClick: () -> Unit,
    onBackToGames: () -> Unit,
    viewModel: SudokuViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SudokuPage(
        board = uiState.board,
        givenCells = uiState.givenCells,
        selectedIndex = uiState.selectedIndex,
        isComplete = uiState.isComplete,
        difficulty = uiState.difficulty,
        onCellSelected = viewModel::selectCell,
        onNumberInput = viewModel::inputNumber,
        onClearSelected = viewModel::clearSelected,
        onUndoMove = viewModel::undoLastMove,
        onToggleDifficulty = viewModel::toggleDifficulty,
        onHelpClick = onHelpClick,
        onBackToGames = onBackToGames,
        onNewGame = viewModel::startNewGame,
        onRestartGame = viewModel::restartCurrentGame,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
private fun SudokuHelpRoute(onReturnToSudoku: () -> Unit) {
    SudokuHelpPage(
        onReturnToSudoku = onReturnToSudoku,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
private fun SettingsRoute(viewModel: SettingsViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsScreen(
        uiState = uiState
    )
}

@Composable
private fun AboutRoute() {
    AboutScreen(modifier = Modifier.padding(24.dp))
}

@Preview(showBackground = true)
@Composable
fun AppRootPreview() {
    EmptyActivityTheme {
        AppRoot()
    }
}
