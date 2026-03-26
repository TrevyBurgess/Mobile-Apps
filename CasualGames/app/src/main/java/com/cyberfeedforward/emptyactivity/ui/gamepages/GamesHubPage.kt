package com.cyberfeedforward.emptyactivity.ui.gamepages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.DeviceHub
import androidx.compose.material.icons.outlined.GridOn
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.ViewModule
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cyberfeedforward.emptyactivity.R
import com.cyberfeedforward.emptyactivity.ui.theme.EmptyActivityTheme

@Composable
fun GamesHubPage(
    onSudokuClick: () -> Unit,
    onMiniSudokuClick: () -> Unit,
    onQueensClick: () -> Unit,
    onLinkedQueensClick: () -> Unit,
    onMahjongClick: () -> Unit,
    onSolitaireClick: () -> Unit,
    onWordleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.games_hub_title),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = stringResource(R.string.games_hub_message),
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyLarge
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                listOf(
                    Triple(R.string.play_sudoku, Icons.Outlined.GridOn, onSudokuClick),
                    Triple(R.string.play_mini_sudoku, Icons.Outlined.Apps, onMiniSudokuClick),
                    Triple(R.string.play_linkedin_queens, Icons.Outlined.DeviceHub, onLinkedQueensClick),
                    Triple(R.string.play_mahjong, Icons.Outlined.ViewModule, onMahjongClick),
                    Triple(R.string.play_solitaire, Icons.Outlined.Style, onSolitaireClick),
                    Triple(R.string.play_wordle, Icons.Outlined.TextFields, onWordleClick)
                )
            ) { (labelRes, icon, onClick) ->
                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = stringResource(labelRes)
                        )
                        Text(text = stringResource(labelRes))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GamesHubPagePreview() {
    EmptyActivityTheme {
        GamesHubPage(
            onSudokuClick = {},
            onMiniSudokuClick = {},
            onQueensClick = {},
            onLinkedQueensClick = {},
            onMahjongClick = {},
            onSolitaireClick = {},
            onWordleClick = {}
        )
    }
}
