package com.cyberfeedforward.emptyactivity.ui.gamepages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Backspace
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
import com.cyberfeedforward.emptyactivity.ui.state.WordleGameStatus
import com.cyberfeedforward.emptyactivity.ui.state.WordleLetterState
import com.cyberfeedforward.emptyactivity.ui.state.WordleUiState
import com.cyberfeedforward.emptyactivity.ui.theme.EmptyActivityTheme

@Composable
fun WordlePage(
    uiState: WordleUiState,
    onInputLetter: (Char) -> Unit,
    onBackspace: () -> Unit,
    onSubmitGuess: () -> Unit,
    onBackToGames: () -> Unit,
    onNewGame: () -> Unit,
    onRestartGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.wordle_title),
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

        WordleBoard(
            uiState = uiState
        )

        Row(
            modifier = Modifier.padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = onNewGame) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = stringResource(R.string.new_game)
                )
            }

            Button(onClick = onRestartGame) {
                Icon(
                    imageVector = Icons.Outlined.RestartAlt,
                    contentDescription = stringResource(R.string.restart_game)
                )
            }

            Button(onClick = onSubmitGuess) {
                Text(text = "Enter")
            }

            Button(onClick = onBackspace) {
                Icon(
                    imageVector = Icons.Outlined.Backspace,
                    contentDescription = "Backspace"
                )
            }
        }

        WordleKeyboard(
            onLetter = onInputLetter,
            onEnter = onSubmitGuess,
            onBackspace = onBackspace,
            enabled = uiState.status == WordleGameStatus.InProgress
        )

        if (uiState.status == WordleGameStatus.Won) {
            Text(
                text = stringResource(R.string.wordle_solved),
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        } else if (uiState.status == WordleGameStatus.Lost) {
            Text(
                text = stringResource(R.string.wordle_failed),
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun WordleBoard(uiState: WordleUiState) {
    val rows = 6
    val cols = 5

    Column(modifier = Modifier.padding(top = 8.dp)) {
        for (row in 0 until rows) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val guess = when {
                    row < uiState.guesses.size -> uiState.guesses[row]
                    row == uiState.guesses.size -> uiState.currentGuess
                    else -> ""
                }

                val evaluation: List<WordleLetterState>? =
                    if (row < uiState.lastEvaluations.size) uiState.lastEvaluations[row] else null

                for (col in 0 until cols) {
                    val letter = guess.getOrNull(col)?.toString() ?: ""
                    val state = evaluation?.getOrNull(col)

                    WordleTile(
                        letter = letter,
                        state = state
                    )
                }
            }
        }
    }
}

@Composable
private fun WordleTile(
    letter: String,
    state: WordleLetterState?
) {
    val baseBorder = MaterialTheme.colorScheme.outline
    val background = when (state) {
        WordleLetterState.Correct -> Color(0xFF2E7D32)
        WordleLetterState.Present -> Color(0xFFF9A825)
        WordleLetterState.Absent -> Color(0xFF616161)
        null -> MaterialTheme.colorScheme.surface
    }

    val contentColor = if (state == null) {
        MaterialTheme.colorScheme.onSurface
    } else {
        Color.White
    }

    val borderColor = if (state == null) baseBorder else background

    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .size(52.dp)
            .border(2.dp, borderColor)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WordleKeyboard(
    onLetter: (Char) -> Unit,
    onEnter: () -> Unit,
    onBackspace: () -> Unit,
    enabled: Boolean
) {
    val rows = listOf(
        "QWERTYUIOP",
        "ASDFGHJKL",
        "ZXCVBNM"
    )

    Column(modifier = Modifier.padding(top = 16.dp)) {
        rows.forEachIndexed { rowIndex, letters ->
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                maxItemsInEachRow = letters.length + if (rowIndex == 2) 2 else 0
            ) {
                if (rowIndex == 2) {
                    Button(onClick = onEnter, enabled = enabled) {
                        Text(text = "Enter")
                    }
                }

                letters.forEach { ch ->
                    Button(
                        onClick = { onLetter(ch) },
                        enabled = enabled,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.size(width = 40.dp, height = 52.dp)
                    ) {
                        Text(text = ch.toString())
                    }
                }

                if (rowIndex == 2) {
                    Button(onClick = onBackspace, enabled = enabled) {
                        Icon(
                            imageVector = Icons.Outlined.Backspace,
                            contentDescription = "Backspace"
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WordlePagePreview() {
    EmptyActivityTheme {
        WordlePage(
            uiState = WordleUiState(
                secret = "APPLE",
                guesses = listOf("ABOUT"),
                currentGuess = "AP",
                status = WordleGameStatus.InProgress,
                lastEvaluations = listOf(
                    listOf(
                        WordleLetterState.Correct,
                        WordleLetterState.Absent,
                        WordleLetterState.Absent,
                        WordleLetterState.Absent,
                        WordleLetterState.Absent
                    )
                )
            ),
            onInputLetter = {},
            onBackspace = {},
            onSubmitGuess = {},
            onBackToGames = {},
            onNewGame = {},
            onRestartGame = {}
        )
    }
}
