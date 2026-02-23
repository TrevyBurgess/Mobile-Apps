package com.cyberfeedforward.emptyactivity.ui.state

data class SudokuUiState(
    val board: List<Int?> = initialBoard,
    val givenCells: Set<Int> = initialGivenCells,
    val selectedIndex: Int? = null,
    val isComplete: Boolean = false
) {
    companion object {
        val seedBoards: List<List<Int?>> = listOf(
            listOf(
                5, 3, null, null, 7, null, null, null, null,
                6, null, null, 1, 9, 5, null, null, null,
                null, 9, 8, null, null, null, null, 6, null,
                8, null, null, null, 6, null, null, null, 3,
                4, null, null, 8, null, 3, null, null, 1,
                7, null, null, null, 2, null, null, null, 6,
                null, 6, null, null, null, null, 2, 8, null,
                null, null, null, 4, 1, 9, null, null, 5,
                null, null, null, null, 8, null, null, 7, 9
            ),
            listOf(
                null, 2, null, 6, null, 8, null, null, null,
                5, 8, null, null, null, 9, 7, null, null,
                null, null, null, null, 4, null, null, null, null,
                3, 7, null, null, null, null, 5, null, null,
                6, null, null, null, null, null, null, null, 4,
                null, null, 8, null, null, null, null, 1, 3,
                null, null, null, null, 2, null, null, null, null,
                null, null, 9, 8, null, null, null, 3, 6,
                null, null, null, 3, null, 6, null, 9, null
            )
        )

        val initialBoard: List<Int?> = seedBoards.first()
        val initialGivenCells: Set<Int> = initialBoard.mapIndexedNotNull { index, value ->
            if (value != null) index else null
        }.toSet()

        fun fromSeed(seedBoard: List<Int?>): SudokuUiState = SudokuUiState(
            board = seedBoard,
            givenCells = seedBoard.mapIndexedNotNull { index, value ->
                if (value != null) index else null
            }.toSet(),
            selectedIndex = null,
            isComplete = false
        )
    }
}
