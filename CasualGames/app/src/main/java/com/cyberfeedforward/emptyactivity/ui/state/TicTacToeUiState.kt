package com.cyberfeedforward.emptyactivity.ui.state

enum class TicTacToePlayer {
    X,
    O
}

data class TicTacToeUiState(
    val boardSize: Int = 3,
    val board: List<TicTacToePlayer?> = List(3 * 3) { null },
    val currentPlayer: TicTacToePlayer = TicTacToePlayer.X,
    val winner: TicTacToePlayer? = null,
    val isDraw: Boolean = false
)
