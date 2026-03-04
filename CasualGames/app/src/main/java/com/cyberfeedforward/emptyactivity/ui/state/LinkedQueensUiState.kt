package com.cyberfeedforward.emptyactivity.ui.state

enum class LinkedQueensDifficulty {
    Easy,
    Hard
}

data class LinkedQueensUiState(
    val boardSize: Int = 5,
    val regions: List<Int> = List(25) { 0 },
    val queens: Set<Int> = emptySet(),
    val isComplete: Boolean = false,
    val hintsEnabled: Boolean = false,
    val difficulty: LinkedQueensDifficulty = LinkedQueensDifficulty.Easy
)
