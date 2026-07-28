package com.cyberfeedforward.fidgetgames.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = viewModel { SettingsViewModel() }
) {
    val uiState by viewModel.uiState.collectAsState()
    
    SettingsScreen(
        uiState = uiState,
        onToggleNotifications = viewModel::onToggleNotification
    )
}

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onToggleNotifications: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = uiState.title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Notifications")
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = uiState.notificationsEnabled,
                onCheckedChange = { onToggleNotifications() }
            )
        }
    }
}
