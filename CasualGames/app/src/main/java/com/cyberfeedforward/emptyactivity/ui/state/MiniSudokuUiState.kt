package com.cyberfeedforward.emptyactivity.ui.state

enum class MiniSudokuDifficulty {
    Easy,
    Hard
}

data class MiniSudokuUiState(
    val board: List<Int?> = initialBoard,
    val givenCells: Set<Int> = initialGivenCells,
    val selectedIndex: Int? = null,
    val isComplete: Boolean = false,
    val difficulty: MiniSudokuDifficulty = MiniSudokuDifficulty.Easy
) {
    companion object {
        val seedBoards: List<List<Int?>> = listOf(
            listOf(
                1, null, null, 4,
                null, 4, 1, null,
                null, 1, 4, null,
                4, null, null, 1
            ),
            listOf(
                null, 3, 2, null,
                2, null, null, 3,
                3, null, null, 1,
                null, 1, 3, null
            )
        )

        val initialBoard: List<Int?> = seedBoards.first()
        val initialGivenCells: Set<Int> = initialBoard.mapIndexedNotNull { index, value ->
            if (value != null) index else null
        }.toSet()

        fun fromSeed(seedBoard: List<Int?>): MiniSudokuUiState = MiniSudokuUiState(
            board = seedBoard,
            givenCells = seedBoard.mapIndexedNotNull { index, value ->
                if (value != null) index else null
            }.toSet(),
            selectedIndex = null,
            isComplete = false
        )
    }
}
