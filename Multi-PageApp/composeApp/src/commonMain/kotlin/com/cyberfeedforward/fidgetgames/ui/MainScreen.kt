package com.cyberfeedforward.fidgetgames.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cyberfeedforward.fidgetgames.ui.home.HomeRoute
import com.cyberfeedforward.fidgetgames.ui.about.AboutRoute
import com.cyberfeedforward.fidgetgames.ui.navigation.Destination
import com.cyberfeedforward.fidgetgames.ui.profile.ProfileRoute
import com.cyberfeedforward.fidgetgames.ui.settings.SettingsRoute

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.hasRoute<Destination.Home>() } == true,
                    onClick = {
                        navController.navigate(Destination.Home) {
                            popUpTo(Destination.Home) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.hasRoute<Destination.Profile>() } == true,
                    onClick = {
                        navController.navigate(Destination.Profile) {
                            popUpTo(Destination.Home) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") }
                )
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.hasRoute<Destination.Settings>() } == true,
                    onClick = {
                        navController.navigate(Destination.Settings) {
                            popUpTo(Destination.Home) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") }
                )
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.hasRoute<Destination.About>() } == true,
                    onClick = {
                        navController.navigate(Destination.About) {
                            popUpTo(Destination.Home) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.Info, contentDescription = "About") },
                    label = { Text("About") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Destination.Home> {
                HomeRoute()
            }
            composable<Destination.Profile> {
                ProfileRoute()
            }
            composable<Destination.Settings> {
                SettingsRoute()
            }
            composable<Destination.About> {
                AboutRoute()
            }
        }
    }
}
