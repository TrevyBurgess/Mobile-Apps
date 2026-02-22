package com.cyberfeedforward.emptyactivity.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cyberfeedforward.emptyactivity.ui.navigation.AppDestination
import com.cyberfeedforward.emptyactivity.ui.screens.GamesScreen
import com.cyberfeedforward.emptyactivity.ui.screens.HomeScreen
import com.cyberfeedforward.emptyactivity.ui.screens.SettingsScreen
import com.cyberfeedforward.emptyactivity.ui.theme.EmptyActivityTheme
import com.cyberfeedforward.emptyactivity.ui.viewmodel.GamesViewModel
import com.cyberfeedforward.emptyactivity.ui.viewmodel.HomeViewModel
import com.cyberfeedforward.emptyactivity.ui.viewmodel.SettingsViewModel

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
                        label = { Text(text = destination.label) },
                        icon = {
                            when (destination) {
                                AppDestination.Home -> Icon(
                                    imageVector = Icons.Outlined.Home,
                                    contentDescription = destination.label
                                )

                                AppDestination.Settings -> Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = destination.label
                                )

                                else -> Unit
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
        startDestination = AppDestination.Home.route,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        composable(AppDestination.Home.route) {
            HomeRoute()
        }
        composable(AppDestination.Games.route) {
            GamesRoute()
        }
        composable(AppDestination.Settings.route) {
            SettingsRoute()
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
private fun GamesRoute(viewModel: GamesViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    GamesScreen(
        uiState = uiState
    )
}

@Composable
private fun SettingsRoute(viewModel: SettingsViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsScreen(
        uiState = uiState
    )
}

@Preview(showBackground = true)
@Composable
fun AppRootPreview() {
    EmptyActivityTheme {
        AppRoot()
    }
}
