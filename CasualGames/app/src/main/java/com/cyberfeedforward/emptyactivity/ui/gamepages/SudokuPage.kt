package com.cyberfeedforward.emptyactivity.ui.gamepages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.cyberfeedforward.emptyactivity.R
import com.cyberfeedforward.emptyactivity.ui.state.SudokuUiState
import com.cyberfeedforward.emptyactivity.ui.theme.EmptyActivityTheme

@Composable
fun SudokuPage(
    board: List<Int?>,
    givenCells: Set<Int>,
    selectedIndex: Int?,
    isComplete: Boolean,
    onCellSelected: (Int) -> Unit,
    onNumberInput: (Int) -> Unit,
    onClearSelected: () -> Unit,
    onBackToGames: () -> Unit,
    onNewGame: () -> Unit,
    onRestartGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.sudoku_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary)

        SudokuGrid(
            board = board,
            givenCells = givenCells,
            selectedIndex = selectedIndex,
            onCellSelected = onCellSelected
        )

        Row(
            modifier = Modifier
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onNewGame
            ) {
                Text(text = stringResource(R.string.new_game))
            }

            Button(
                onClick = onClearSelected,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Backspace,
                    contentDescription = stringResource(R.string.erase)
                )
//                Text(
//                    text = stringResource(R.string.erase),
//                    modifier = Modifier.padding(start = 6.dp)
//                )
            }
        }

        NumberPad(
            onNumberInput = onNumberInput
        )

        Row(
            modifier = Modifier.padding(top = 12.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = onRestartGame) {
                Text(text = stringResource(R.string.restart_game))
            }

            Button(onClick = onBackToGames) {
                Text(text = stringResource(R.string.back_to_games))
            }
        }

        if (isComplete) {
            Text(
                text = stringResource(R.string.sudoku_solved),
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
private fun NumberPad(onNumberInput: (Int) -> Unit) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = 5
    ) {
        (1..9).forEach { number ->
            Button(
                onClick = { onNumberInput(number) },
                shape = MaterialTheme.shapes.small,

                modifier = Modifier.size(50.dp)

            ) {
                Column {
                    Text(
                        text = number.toString(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontSize = 6.em,
                        modifier = Modifier.padding(0.dp)
                    )

//                    Text(
//                        text = number.toString(),
//                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
//                        fontSize = 4.em,
//                        modifier = Modifier.padding(0.dp)
//                    )
                }
            }
        }
    }
}

@Composable
private fun SudokuGrid(
    board: List<Int?>,
    givenCells: Set<Int>,
    selectedIndex: Int?,
    onCellSelected: (Int) -> Unit
) {
    val heavyLineColor = Color.Black
    val heavyLineWidth = 3.dp

    Box(
        modifier = Modifier.drawWithContent {
            drawContent()

            val strokeWidth = heavyLineWidth.toPx()
            val oneThirdWidth = size.width / 3f
            val oneThirdHeight = size.height / 3f

            for (i in 1..2) {
                val x = oneThirdWidth * i
                drawLine(
                    color = heavyLineColor,
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = strokeWidth
                )

                val y = oneThirdHeight * i
                drawLine(
                    color = heavyLineColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }

            drawRect(
                color = heavyLineColor,
                style = Stroke(width = strokeWidth)
            )
        }
    ) {
        Column {
            for (row in 0 until 9) {
                Row {
                    for (col in 0 until 9) {
                        val index = row * 9 + col
                        SudokuCell(
                            value = board[index],
                            isGiven = givenCells.contains(index),
                            isSelected = selectedIndex == index,
                            onClick = { onCellSelected(index) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SudokuCell(
    value: Int?,
    isGiven: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline
    }

    val backgroundColor = if (isSelected) {
        Color(0xFFADD8E6)
    } else if (isGiven) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.surface
    }

    Box(
        modifier = Modifier
            .size(37.dp)
            .border(width = 1.dp, color = borderColor)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value?.toString().orEmpty(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isGiven) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SudokuPagePreview() {
    EmptyActivityTheme {
        SudokuPage(
            board = SudokuUiState.initialBoard,
            givenCells = SudokuUiState.initialGivenCells,
            selectedIndex = null,
            isComplete = false,
            onCellSelected = {},
            onNumberInput = {},
            onClearSelected = {},
            onBackToGames = {},
            onNewGame = {},
            onRestartGame = {}
        )
    }
}
