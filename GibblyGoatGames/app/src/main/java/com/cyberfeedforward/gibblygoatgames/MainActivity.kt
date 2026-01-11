package com.cyberfeedforward.gibblygoatgames

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cyberfeedforward.gibblygoatgames.ui.compose.parts.AppBarBottom
import com.cyberfeedforward.gibblygoatgames.ui.compose.parts.AppBarTop
import com.cyberfeedforward.gibblygoatgames.ui.compose.parts.AppNavHost
import com.cyberfeedforward.gibblygoatgames.ui.theme.GibblyGoatGamesTheme
import com.cyberfeedforward.gibblygoatgames.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainContent()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainContent() {
    val viewModel = viewModel<MainViewModel>()
    val navController: NavHostController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination: NavDestination? = navBackStackEntry?.destination

    GibblyGoatGamesTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                val context = LocalContext.current
                val shareMessage = R.string.share_message

                AppBarTop(
                    shareActivity = {
                        viewModel.shareActivity(
                            context,
                            shareMessage,
                        )
                    }
                )
            },
            bottomBar = {
                AppBarBottom(
                    navController = navController,
                    currentDestination = currentDestination,
                    viewmModel = viewModel,
                )
            }
            ) { innerPadding ->

            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}