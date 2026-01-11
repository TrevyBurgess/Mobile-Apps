package com.cyberfeedforward.gibblygoatgames.ui.compose.parts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.cyberfeedforward.gibblygoatgames.R
import com.cyberfeedforward.gibblygoatgames.models.screenList
import com.cyberfeedforward.gibblygoatgames.viewmodels.MainViewModel

@Composable
fun AppBarBottom(
    navController: NavHostController,
    currentDestination: NavDestination?,
    viewmModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier,
    ) {
        val quantity by viewmModel.quantity.collectAsState

        screenList.forEach { screen ->
            val label = stringResource(screen.labelResourceId)

            NavigationBarItem(
                icon = {
                    BadgedBox(
                        badge = {
                            if (quantity > 0) {
                                Badge { Text(text = quantity.toString()) }
                            }
                        }
                            ) {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = label
                            )
                    }
                },
                label = {
                    Text(text = label)
                },
                selected = currentDestination?.hierarchy?.any {
                    it.route == screen.route
                } == true,
                onClick = {
                    navController.navigate(screen.route){
                        popUpTo(navController.graph.startDestinationId){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}