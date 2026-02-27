package com.cyberfeedforward.emptyactivity.ui.gamepages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Undo
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cyberfeedforward.emptyactivity.R
import com.cyberfeedforward.emptyactivity.ui.state.QueensDifficulty
import com.cyberfeedforward.emptyactivity.ui.theme.EmptyActivityTheme

@Composable
fun QueensPage(
    boardSize: Int,
    queens: Set<Int>,
    givenQueens: Set<Int>,
    isComplete: Boolean,
    difficulty: QueensDifficulty,
    hintsEnabled: Boolean,
    onCellToggle: (Int) -> Unit,
    onToggleDifficulty: () -> Unit,
    onToggleHints: () -> Unit,
    onBackToGames: () -> Unit,
    onNewGame: () -> Unit,
    onUndoMove: () -> Unit,
    onRestartGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    val difficultyText = if (difficulty == QueensDifficulty.Easy) {
        stringResource(R.string.queens_difficulty_easy)
    } else {
        stringResource(R.string.queens_difficulty_hard)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.queens_title) + " - " + difficultyText,
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

        QueensGrid(
            boardSize = boardSize,
            queens = queens,
            givenQueens = givenQueens,
            hintsEnabled = hintsEnabled,
            onCellToggle = onCellToggle
        )

        Row(
            modifier = Modifier.padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = onToggleDifficulty) {
                Text(
                    text = if (difficulty == QueensDifficulty.Easy) {
                        stringResource(R.string.queens_difficulty_hard)
                    } else {
                        stringResource(R.string.queens_difficulty_easy)
                    }
                )
            }

            Button(onClick = onToggleHints) {
                Text(
                    text = if (hintsEnabled) {
                        stringResource(R.string.hints_off)
                    } else {
                        stringResource(R.string.hints_on)
                    }
                )
            }

            Button(onClick = onNewGame) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = stringResource(R.string.new_game)
                )
            }

            Button(onClick = onUndoMove) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Undo,
                    contentDescription = stringResource(R.string.undo_move)
                )
            }

            Button(onClick = onRestartGame) {
                Icon(
                    imageVector = Icons.Outlined.RestartAlt,
                    contentDescription = stringResource(R.string.restart_game)
                )
            }
        }

        if (isComplete) {
            Text(
                text = stringResource(R.string.queens_solved),
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun hasConflict(index: Int, queens: Set<Int>, boardSize: Int): Boolean {
    if (index !in queens) return false

    val row = index / boardSize
    val col = index % boardSize

    return queens.any { other ->
        if (other == index) {
            false
        } else {
            val otherRow = other / boardSize
            val otherCol = other % boardSize
            row == otherRow || col == otherCol || kotlin.math.abs(row - otherRow) == kotlin.math.abs(col - otherCol)
        }
    }
}

@Composable
private fun QueensGrid(
    boardSize: Int,
    queens: Set<Int>,
    givenQueens: Set<Int>,
    hintsEnabled: Boolean,
    onCellToggle: (Int) -> Unit
) {
    Column {
        for (row in 0 until boardSize) {
            Row {
                for (col in 0 until boardSize) {
                    val index = row * boardSize + col
                    val isDark = (row + col) % 2 == 1
                    val baseColor = if (isDark) Color(0xFFB58863) else Color(0xFFF0D9B5)
                    val inConflict = hintsEnabled && hasConflict(index, queens, boardSize)
                    val borderColor = if (inConflict) Color(0xFFD32F2F) else MaterialTheme.colorScheme.outline

                    Box(
                        modifier = Modifier
                            .size(if (boardSize <= 5) 56.dp else 36.dp)
                            .border(2.dp, borderColor)
                            .background(baseColor)
                            .clickable { onCellToggle(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (index in queens) {
                            Text(
                                text = "Q",
                                color = if (index in givenQueens) {
                                    MaterialTheme.colorScheme.onSurface
                                } else {
                                    MaterialTheme.colorScheme.primary
                                },
                                fontWeight = if (index in givenQueens) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Medium
                                }
                            )
                        }

                        if (inConflict && index in queens) {
                            Text(
                                text = "!",
                                color = Color(0xFFD32F2F),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(end = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QueensPagePreview() {
    EmptyActivityTheme {
        QueensPage(
            boardSize = 5,
            queens = setOf(1, 8),
            givenQueens = setOf(1),
            isComplete = false,
            difficulty = QueensDifficulty.Easy,
            hintsEnabled = true,
            onCellToggle = {},
            onToggleDifficulty = {},
            onToggleHints = {},
            onBackToGames = {},
            onNewGame = {},
            onUndoMove = {},
            onRestartGame = {}
        )
    }
}
