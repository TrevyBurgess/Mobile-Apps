package com.cyberfeedforward.emptyactivity.ui.gamepages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cyberfeedforward.emptyactivity.R
import com.cyberfeedforward.emptyactivity.ui.theme.EmptyActivityTheme

@Composable
fun SolitairePage(
    onBackToGames: () -> Unit,
    modifier: Modifier = Modifier
) {
    val deck = remember { mutableStateListOf<Int>().apply { addAll(generateDeck()) } }
    var foundationTop by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.solitaire_title),
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

        Text(
            text = stringResource(R.string.solitaire_instruction),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(R.string.solitaire_deck))
                CardSlot(label = deck.lastOrNull()?.toString() ?: "-")
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(R.string.solitaire_foundation))
                CardSlot(label = if (foundationTop == 0) "-" else foundationTop.toString())
            }
        }

        Button(
            onClick = {
                val topCard = deck.lastOrNull() ?: return@Button
                if (topCard == foundationTop + 1) {
                    deck.removeAt(deck.lastIndex)
                    foundationTop = topCard
                }
            },
            enabled = deck.isNotEmpty(),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = stringResource(R.string.solitaire_play_card))
        }

        Button(
            onClick = {
                deck.clear()
                deck.addAll(generateDeck())
                foundationTop = 0
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Refresh,
                contentDescription = stringResource(R.string.new_game)
            )
            Text(
                text = stringResource(R.string.new_game),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        if (deck.isEmpty() && foundationTop == 13) {
            Text(
                text = stringResource(R.string.solitaire_solved),
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CardSlot(label: String) {
    Box(
        modifier = Modifier
            .size(width = 72.dp, height = 96.dp)
            .border(1.dp, Color.DarkGray)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

private fun generateDeck(): List<Int> {
    return (1..13).toList().shuffled()
}

@Preview(showBackground = true)
@Composable
private fun SolitairePagePreview() {
    EmptyActivityTheme {
        SolitairePage(onBackToGames = {})
    }
}
