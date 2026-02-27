package com.cyberfeedforward.emptyactivity.ui.gamepages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
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
        Button(
            onClick = onSudokuClick,
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(text = stringResource(R.string.play_sudoku))
        }

        Button(
            onClick = onMiniSudokuClick,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text(text = stringResource(R.string.play_mini_sudoku))
        }

//        Button(
//            onClick = onQueensClick,
//            modifier = Modifier.padding(top = 12.dp)
//        ) {
//            Text(text = stringResource(R.string.play_queens))
//        }

        Button(
            onClick = onLinkedQueensClick,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text(text = stringResource(R.string.play_linkedin_queens))
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
            onLinkedQueensClick = {}
        )
    }
}
