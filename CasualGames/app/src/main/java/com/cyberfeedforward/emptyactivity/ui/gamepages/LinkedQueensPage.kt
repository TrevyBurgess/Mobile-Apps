package com.cyberfeedforward.emptyactivity.ui.gamepages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cyberfeedforward.emptyactivity.R
import com.cyberfeedforward.emptyactivity.ui.state.LinkedQueensDifficulty
import com.cyberfeedforward.emptyactivity.ui.theme.EmptyActivityTheme
import kotlin.math.abs

@Composable
fun LinkedQueensPage(
    boardSize: Int,
    regions: List<Int>,
    queens: Set<Int>,
    isComplete: Boolean,
    difficulty: LinkedQueensDifficulty,
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
    val difficultyText = if (difficulty == LinkedQueensDifficulty.Easy) {
        stringResource(R.string.linkedin_queens_difficulty_easy)
    } else {
        stringResource(R.string.linkedin_queens_difficulty_hard)
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.linkedin_queens_title) + " - " + difficultyText,
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

        LinkedQueensGrid(
            boardSize = boardSize,
            regions = regions,
            queens = queens,
            hintsEnabled = hintsEnabled,
            onCellToggle = onCellToggle
        )

        Row(modifier = Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {


            Button(onClick = onUndoMove) {
                Icon(Icons.AutoMirrored.Outlined.Undo, contentDescription = stringResource(R.string.undo_move))
            }
            Button(onClick = onRestartGame) {
                Icon(Icons.Outlined.RestartAlt, contentDescription = stringResource(R.string.restart_game))
            }
        }

        Row(modifier = Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onNewGame) {
                Icon(Icons.Outlined.Add, contentDescription = stringResource(R.string.new_game))
            }

            Button(onClick = onToggleDifficulty) {
                Text(
                    text = if (difficulty == LinkedQueensDifficulty.Easy) {
                        stringResource(R.string.linkedin_queens_difficulty_hard)
                    } else {
                        stringResource(R.string.linkedin_queens_difficulty_easy)
                    }
                )
            }

            Button(onClick = onToggleHints) {
                Text(text = if (hintsEnabled) stringResource(R.string.hints_off) else stringResource(R.string.hints_on))
            }

        }


        if (isComplete) {
            Text(
                text = stringResource(R.string.linkedin_queens_solved),
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun LinkedQueensGrid(
    boardSize: Int,
    regions: List<Int>,
    queens: Set<Int>,
    hintsEnabled: Boolean,
    onCellToggle: (Int) -> Unit
) {
    val regionColors = listOf(
        Color(0xFFE3F2FD),
        Color(0xFFF1F8E9),
        Color(0xFFFFF3E0),
        Color(0xFFFCE4EC),
        Color(0xFFE8EAF6),
        Color(0xFFE0F2F1),
        Color(0xFFFFFDE7),
        Color(0xFFEDE7F6)
    )

    Column {
        for (row in 0 until boardSize) {
            Row {
                for (col in 0 until boardSize) {
                    val index = row * boardSize + col
                    val regionId = regions[index]
                    val baseColor = regionColors[regionId % regionColors.size]
                    val inConflict = hintsEnabled && hasLinkedQueensConflict(index, queens, regions, boardSize)

                    val hasTopBoundary = row == 0 || regions[(row - 1) * boardSize + col] != regionId
                    val hasBottomBoundary =
                        row == boardSize - 1 || regions[(row + 1) * boardSize + col] != regionId
                    val hasLeftBoundary = col == 0 || regions[row * boardSize + (col - 1)] != regionId
                    val hasRightBoundary =
                        col == boardSize - 1 || regions[row * boardSize + (col + 1)] != regionId

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(baseColor)
                            .drawWithContent {
                                drawContent()

                                drawRect(
                                    color = Color(0xFF555555),
                                    style = Stroke(width = 1.dp.toPx())
                                )

                                val stroke = 3.dp.toPx()
                                if (hasTopBoundary) {
                                    drawLine(
                                        color = Color.Black,
                                        start = Offset(0f, 0f),
                                        end = Offset(size.width, 0f),
                                        strokeWidth = stroke
                                    )
                                }
                                if (hasBottomBoundary) {
                                    drawLine(
                                        color = Color.Black,
                                        start = Offset(0f, size.height),
                                        end = Offset(size.width, size.height),
                                        strokeWidth = stroke
                                    )
                                }
                                if (hasLeftBoundary) {
                                    drawLine(
                                        color = Color.Black,
                                        start = Offset(0f, 0f),
                                        end = Offset(0f, size.height),
                                        strokeWidth = stroke
                                    )
                                }
                                if (hasRightBoundary) {
                                    drawLine(
                                        color = Color.Black,
                                        start = Offset(size.width, 0f),
                                        end = Offset(size.width, size.height),
                                        strokeWidth = stroke
                                    )
                                }
                            }
                            .clickable { onCellToggle(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (index in queens) {
                            Image(
                                painter = painterResource(id = R.drawable.queen),
                                contentDescription = stringResource(R.string.queens_title),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                contentScale = ContentScale.Fit
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

private fun hasLinkedQueensConflict(
    index: Int,
    queens: Set<Int>,
    regions: List<Int>,
    boardSize: Int
): Boolean {
    if (index !in queens) return false

    val row = index / boardSize
    val col = index % boardSize
    val region = regions[index]

    return queens.any { other ->
        if (other == index) {
            false
        } else {
            val otherRow = other / boardSize
            val otherCol = other % boardSize
            val sameRow = row == otherRow
            val sameCol = col == otherCol
            val sameDiag = abs(row - otherRow) == abs(col - otherCol)
            val sameRegion = region == regions[other]
            sameRow || sameCol || sameDiag || sameRegion
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LinkedQueensPagePreview() {
    EmptyActivityTheme {
        LinkedQueensPage(
            boardSize = 5,
            regions = List(25) { it % 5 },
            queens = setOf(1, 8),
            isComplete = false,
            difficulty = LinkedQueensDifficulty.Easy,
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
