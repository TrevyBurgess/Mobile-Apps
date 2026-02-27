package com.cyberfeedforward.emptyactivity.ui.state

enum class QueensDifficulty {
    Easy,
    Hard
}

data class QueensUiState(
    val boardSize: Int = 5,
    val queens: Set<Int> = emptySet(),
    val givenQueens: Set<Int> = emptySet(),
    val isComplete: Boolean = false,
    val difficulty: QueensDifficulty = QueensDifficulty.Easy,
    val hintsEnabled: Boolean = false
)
