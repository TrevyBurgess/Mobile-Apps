package com.cyberfeedforward.emptyactivity.ui.gamepages

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cyberfeedforward.emptyactivity.R
import com.cyberfeedforward.emptyactivity.ui.state.TicTacToePlayer
import com.cyberfeedforward.emptyactivity.ui.state.TicTacToeUiState
import com.cyberfeedforward.emptyactivity.ui.theme.EmptyActivityTheme

@Composable
fun TicTacToePage(
    uiState: TicTacToeUiState,
    onCellPressed: (Int) -> Unit,
    onBoardSizeDecrease: () -> Unit,
    onBoardSizeIncrease: () -> Unit,
    onRestartGame: () -> Unit,
    onBackToGames: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.tic_tac_toe_title),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(20.dp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Button(onClick = onBackToGames) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.back_to_games)
                )
            }
        }

        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.board_size, uiState.boardSize),
                style = MaterialTheme.typography.titleMedium
            )

            Button(
                onClick = onBoardSizeDecrease,
                enabled = uiState.boardSize > 3
            ) {
                Icon(
                    imageVector = Icons.Outlined.Remove,
                    contentDescription = stringResource(R.string.decrease)
                )
            }

            Button(
                onClick = onBoardSizeIncrease,
                enabled = uiState.boardSize < 10
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = stringResource(R.string.increase)
                )
            }

            Button(onClick = onRestartGame) {
                Icon(
                    imageVector = Icons.Outlined.RestartAlt,
                    contentDescription = stringResource(R.string.restart_game)
                )
            }
        }

        StatusText(uiState = uiState, modifier = Modifier.padding(top = 12.dp))

        TicTacToeBoard(
            uiState = uiState,
            onCellPressed = onCellPressed,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .aspectRatio(1f)
        )
    }
}

@Composable
private fun StatusText(uiState: TicTacToeUiState, modifier: Modifier = Modifier) {
    val text = when {
        uiState.winner == TicTacToePlayer.X -> stringResource(R.string.winner_x)
        uiState.winner == TicTacToePlayer.O -> stringResource(R.string.winner_o)
        uiState.isDraw -> stringResource(R.string.draw)
        uiState.currentPlayer == TicTacToePlayer.X -> stringResource(R.string.x_turn)
        else -> stringResource(R.string.o_turn)
    }

    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun TicTacToeBoard(
    uiState: TicTacToeUiState,
    onCellPressed: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier) {
        val cellSize = rememberCellSize(maxWidth = maxWidth, boardSize = uiState.boardSize)

        LazyVerticalGrid(
            columns = GridCells.Fixed(uiState.boardSize),
            userScrollEnabled = false,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(uiState.board.indices.toList()) { index ->
                val value = uiState.board[index]
                TicTacToeCell(
                    value = value,
                    size = cellSize,
                    enabled = value == null && uiState.winner == null && !uiState.isDraw,
                    onClick = { onCellPressed(index) }
                )
            }
        }
    }
}

@Composable
private fun rememberCellSize(maxWidth: Dp, boardSize: Int): Dp {
    val density = LocalDensity.current
    val spacing = 6.dp

    return with(density) {
        val totalSpacing = spacing.toPx() * (boardSize - 1)
        val available = maxWidth.toPx() - totalSpacing
        (available / boardSize).toDp()
    }
}

@Composable
private fun TicTacToeCell(
    value: TicTacToePlayer?,
    size: Dp,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .border(2.dp, MaterialTheme.colorScheme.outline)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (value) {
                TicTacToePlayer.X -> "X"
                TicTacToePlayer.O -> "O"
                null -> ""
            },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TicTacToePagePreview() {
    EmptyActivityTheme {
        TicTacToePage(
            uiState = TicTacToeUiState(),
            onCellPressed = {},
            onBoardSizeDecrease = {},
            onBoardSizeIncrease = {},
            onRestartGame = {},
            onBackToGames = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
