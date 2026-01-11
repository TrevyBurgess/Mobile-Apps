package com.cyberfeedforward.gibblygoatgames.ui.compose.parts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.cyberfeedforward.gibblygoatgames.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarTop(
    shareActivity: () -> Unit,

) {
    // Share - Partager
    val navShare = stringResource(R.string.nav_share_game)
    val appName = stringResource(R.string.app_name)

    TopAppBar(
        title = {
            Text(text = appName)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        actions = {
            IconButton(
                onClick = shareActivity
            ) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    tint = Color.White,
                    contentDescription = navShare
                )
            }
        }
    )
}