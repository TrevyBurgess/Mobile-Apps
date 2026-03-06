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
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
fun MahjongPage(
    onBackToGames: () -> Unit,
    modifier: Modifier = Modifier
) {
    var tiles by remember { mutableStateOf(generateMahjongTiles()) }
    val remainingPairs = tiles.count { !it.isMatched } / 2
    val selectedTileIndex = tiles.indexOfFirst { it.isSelected }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.mahjong_title),
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
            text = stringResource(R.string.mahjong_pairs_remaining, remainingPairs),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        for (row in 0 until 4) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (col in 0 until 4) {
                    val index = row * 4 + col
                    val tile = tiles[index]
                    val background = when {
                        tile.isMatched -> MaterialTheme.colorScheme.surfaceVariant
                        tile.isSelected -> MaterialTheme.colorScheme.primaryContainer
                        else -> MaterialTheme.colorScheme.surface
                    }

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .border(1.dp, Color.DarkGray)
                            .background(background)
                            .clickable(enabled = !tile.isMatched) {
                                tiles = onTileClicked(tiles, index)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (tile.isMatched || tile.isSelected || selectedTileIndex == -1) tile.symbol else "?",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }

        Button(
            onClick = { tiles = generateMahjongTiles() },
            modifier = Modifier.padding(top = 12.dp)
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

        if (remainingPairs == 0) {
            Text(
                text = stringResource(R.string.mahjong_solved),
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private data class MahjongTile(
    val symbol: String,
    val isSelected: Boolean = false,
    val isMatched: Boolean = false
)

private fun generateMahjongTiles(): List<MahjongTile> {
    val symbols = listOf("B", "C", "D", "E", "F", "G", "H", "I")
    return (symbols + symbols)
        .shuffled()
        .map { symbol -> MahjongTile(symbol = symbol) }
}

private fun onTileClicked(tiles: List<MahjongTile>, index: Int): List<MahjongTile> {
    val clicked = tiles[index]
    if (clicked.isMatched || clicked.isSelected) return tiles

    val selectedIndex = tiles.indexOfFirst { it.isSelected }
    if (selectedIndex == -1) {
        return tiles.mapIndexed { i, tile ->
            if (i == index) tile.copy(isSelected = true) else tile
        }
    }

    val selected = tiles[selectedIndex]
    return if (selected.symbol == clicked.symbol) {
        tiles.mapIndexed { i, tile ->
            if (i == selectedIndex || i == index) {
                tile.copy(isSelected = false, isMatched = true)
            } else {
                tile
            }
        }
    } else {
        tiles.mapIndexed { i, tile ->
            when (i) {
                selectedIndex -> tile.copy(isSelected = false)
                index -> tile.copy(isSelected = true)
                else -> tile
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MahjongPagePreview() {
    EmptyActivityTheme {
        MahjongPage(onBackToGames = {})
    }
}
