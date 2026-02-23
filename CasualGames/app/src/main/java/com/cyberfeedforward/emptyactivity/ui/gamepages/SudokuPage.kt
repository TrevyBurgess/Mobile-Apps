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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SudokuGrid(
            board = board,
            givenCells = givenCells,
            selectedIndex = selectedIndex,
            onCellSelected = onCellSelected
        )

        Row(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            (1..9).forEach { number ->
                Button(onClick = { onNumberInput(number) }, modifier = Modifier.size(40.dp)) {
                    Text(text = number.toString())
                }
            }
        }

        Button(
            onClick = onClearSelected,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text(text = "Clear")
        }

        if (isComplete) {
            Text(
                text = "Sudoku solved!",
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
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

    val backgroundColor = if (isGiven) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.surface
    }

    Box(
        modifier = Modifier
            .size(36.dp)
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
            onClearSelected = {}
        )
    }
}
