package com.cyberfeedforward.emptyactivity.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cyberfeedforward.emptyactivity.ui.state.QueensDifficulty
import com.cyberfeedforward.emptyactivity.ui.state.QueensUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random

class QueensViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(QueensUiState())
    val uiState: StateFlow<QueensUiState> = _uiState.asStateFlow()

    private var currentGivenQueens: Set<Int> = emptySet()
    private val moveHistory = mutableListOf<Set<Int>>()

    init {
        startNewGame()
    }

    fun toggleCell(index: Int) {
        val current = _uiState.value
        if (index in current.givenQueens) return

        val size = current.boardSize
        if (index !in 0 until (size * size)) return

        moveHistory.add(current.queens)

        val row = index / size
        val nextQueens = current.queens
            .filterNot { it / size == row && it !in current.givenQueens }
            .toMutableSet()

        if (index !in current.queens) {
            nextQueens.add(index)
        }

        _uiState.update {
            it.copy(
                queens = nextQueens,
                isComplete = isSolved(size, nextQueens)
            )
        }
    }

    fun startNewGame() {
        val difficulty = _uiState.value.difficulty
        val hintsEnabled = _uiState.value.hintsEnabled
        val size = if (difficulty == QueensDifficulty.Easy) 5 else 8
        val solution = generateSolution(size)
        val givenQueens = generateGivenQueens(solution, difficulty)
        currentGivenQueens = givenQueens
        moveHistory.clear()

        _uiState.value = QueensUiState(
            boardSize = size,
            queens = givenQueens,
            givenQueens = givenQueens,
            difficulty = difficulty,
            isComplete = isSolved(size, givenQueens),
            hintsEnabled = hintsEnabled
        )
    }

    fun restartCurrentGame() {
        moveHistory.clear()
        _uiState.update {
            it.copy(
                queens = currentGivenQueens,
                isComplete = isSolved(it.boardSize, currentGivenQueens)
            )
        }
    }

    fun toggleDifficulty() {
        val nextDifficulty = if (_uiState.value.difficulty == QueensDifficulty.Easy) {
            QueensDifficulty.Hard
        } else {
            QueensDifficulty.Easy
        }

        _uiState.update { it.copy(difficulty = nextDifficulty) }
        startNewGame()
    }

    fun toggleHints() {
        _uiState.update { it.copy(hintsEnabled = !it.hintsEnabled) }
    }

    fun undoLastMove() {
        val previousQueens = moveHistory.removeLastOrNull() ?: return
        _uiState.update {
            it.copy(
                queens = previousQueens,
                isComplete = isSolved(it.boardSize, previousQueens)
            )
        }
    }

    private fun generateGivenQueens(solution: IntArray, difficulty: QueensDifficulty): Set<Int> {
        val size = solution.size
        val revealCount = if (difficulty == QueensDifficulty.Easy) {
            max(2, size / 2)
        } else {
            max(1, size / 4)
        }

        val revealedRows = (0 until size).shuffled(Random).take(revealCount)
        return revealedRows.map { row -> row * size + solution[row] }.toSet()
    }

    private fun generateSolution(size: Int): IntArray {
        while (true) {
            val columns = IntArray(size) { -1 }
            if (solveRow(0, size, columns)) {
                return columns
            }
        }
    }

    private fun solveRow(row: Int, size: Int, columns: IntArray): Boolean {
        if (row == size) return true

        val candidates = (0 until size).shuffled(Random)
        for (col in candidates) {
            if (isSafe(row, col, columns)) {
                columns[row] = col
                if (solveRow(row + 1, size, columns)) {
                    return true
                }
                columns[row] = -1
            }
        }

        return false
    }

    private fun isSafe(row: Int, col: Int, columns: IntArray): Boolean {
        for (r in 0 until row) {
            val c = columns[r]
            if (c == col) return false
            if (abs(c - col) == abs(r - row)) return false
        }
        return true
    }

    private fun isSolved(size: Int, queens: Set<Int>): Boolean {
        if (queens.size != size) return false

        val rows = mutableSetOf<Int>()
        val cols = mutableSetOf<Int>()
        val diagDown = mutableSetOf<Int>()
        val diagUp = mutableSetOf<Int>()

        queens.forEach { index ->
            val row = index / size
            val col = index % size

            if (!rows.add(row)) return false
            if (!cols.add(col)) return false
            if (!diagDown.add(row - col)) return false
            if (!diagUp.add(row + col)) return false
        }

        return true
    }
}
