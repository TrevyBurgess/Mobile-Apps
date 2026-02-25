package com.cyberfeedforward.emptyactivity.ui.gamepages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cyberfeedforward.emptyactivity.R
import com.cyberfeedforward.emptyactivity.ui.state.MiniSudokuDifficulty
import com.cyberfeedforward.emptyactivity.ui.state.MiniSudokuUiState
import com.cyberfeedforward.emptyactivity.ui.theme.EmptyActivityTheme

@Composable
fun MiniSudokuPage(
    board: List<Int?>,
    givenCells: Set<Int>,
    selectedIndex: Int?,
    isComplete: Boolean,
    difficulty: MiniSudokuDifficulty,
    onCellSelected: (Int) -> Unit,
    onNumberInput: (Int) -> Unit,
    onUndoMove: () -> Unit,
    onToggleDifficulty: () -> Unit,
    onBackToGames: () -> Unit,
    onNewGame: () -> Unit,
    onRestartGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val difficultyTesy = if (difficulty == MiniSudokuDifficulty.Easy) {
            stringResource(R.string.mini_sudoku_difficulty_easy)
        } else {
            stringResource(R.string.mini_sudoku_difficulty_hard)
        }

        Row(
            modifier = Modifier.padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = stringResource(R.string.mini_sudoku_title) + " - " + difficultyTesy,
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

        MiniSudokuGrid(
            board = board,
            givenCells = givenCells,
            selectedIndex = selectedIndex,
            onCellSelected = onCellSelected
        )

        Row(
            modifier = Modifier.padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = onToggleDifficulty) {
                Text(
                    text = if (difficulty == MiniSudokuDifficulty.Easy) {
                        stringResource(R.string.mini_sudoku_difficulty_hard)
                    } else {
                        stringResource(R.string.mini_sudoku_difficulty_easy)
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

        MiniNumberPad(onNumberInput = onNumberInput)

        if (isComplete) {
            Text(
                text = stringResource(R.string.mini_sudoku_solved),
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MiniNumberPad(onNumberInput: (Int) -> Unit) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = 4
    ) {
        (1..4).forEach { number ->
            Button(
                onClick = { onNumberInput(number) },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.size(64.dp)
            ) {
                Text(text = number.toString(), style = MaterialTheme.typography.headlineSmall)
            }
        }
    }
}

@Composable
private fun MiniSudokuGrid(
    board: List<Int?>,
    givenCells: Set<Int>,
    selectedIndex: Int?,
    onCellSelected: (Int) -> Unit
) {
    val heavyLineColor = Color.Black
    val heavyLineWidth = 3.dp
    val selectedRow = selectedIndex?.div(4)
    val selectedCol = selectedIndex?.rem(4)
    val selectedSubgridRow = selectedRow?.div(2)
    val selectedSubgridCol = selectedCol?.div(2)

    Box(
        modifier = Modifier.drawWithContent {
            drawContent()

            val strokeWidth = heavyLineWidth.toPx()
            val halfWidth = size.width / 2f
            val halfHeight = size.height / 2f

            drawLine(
                color = heavyLineColor,
                start = Offset(halfWidth, 0f),
                end = Offset(halfWidth, size.height),
                strokeWidth = strokeWidth
            )

            drawLine(
                color = heavyLineColor,
                start = Offset(0f, halfHeight),
                end = Offset(size.width, halfHeight),
                strokeWidth = strokeWidth
            )

            drawRect(
                color = heavyLineColor,
                style = Stroke(width = strokeWidth)
            )
        }
    ) {
        Column {
            for (row in 0 until 4) {
                Row {
                    for (col in 0 until 4) {
                        val index = row * 4 + col
                        val isRowMatch = selectedRow == row
                        val isColMatch = selectedCol == col
                        val isSubgridMatch =
                            selectedSubgridRow == row / 2 && selectedSubgridCol == col / 2

                        MiniSudokuCell(
                            value = board[index],
                            isGiven = givenCells.contains(index),
                            isSelected = selectedIndex == index,
                            isHighlighted = selectedIndex != null &&
                                (isRowMatch || isColMatch || isSubgridMatch),
                            onClick = { onCellSelected(index) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniSudokuCell(
    value: Int?,
    isGiven: Boolean,
    isSelected: Boolean,
    isHighlighted: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline
    }

    val backgroundColor = if (isSelected) {
        Color(0xFFADD8E6)
    } else if (isHighlighted) {
        Color(0xFFD3D3D3)
    } else if (isGiven) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.surface
    }

    Box(
        modifier = Modifier
            .size(56.dp)
            .border(width = 1.dp, color = borderColor)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value?.toString().orEmpty(),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = if (isGiven) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MiniSudokuPagePreview() {
    EmptyActivityTheme {
        MiniSudokuPage(
            board = MiniSudokuUiState.initialBoard,
            givenCells = MiniSudokuUiState.initialGivenCells,
            selectedIndex = null,
            isComplete = false,
            difficulty = MiniSudokuDifficulty.Easy,
            onCellSelected = {},
            onNumberInput = {},
            onUndoMove = {},
            onToggleDifficulty = {},
            onBackToGames = {},
            onNewGame = {},
            onRestartGame = {}
        )
    }
}
