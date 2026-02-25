package com.cyberfeedforward.emptyactivity.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cyberfeedforward.emptyactivity.ui.state.MiniSudokuDifficulty
import com.cyberfeedforward.emptyactivity.ui.state.MiniSudokuUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class MiniSudokuViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MiniSudokuUiState())
    val uiState: StateFlow<MiniSudokuUiState> = _uiState.asStateFlow()
    private var currentSeedBoard: List<Int?> = MiniSudokuUiState.initialBoard
    private val moveHistory = mutableListOf<List<Int?>>()

    init {
        startNewGame()
    }

    fun selectCell(index: Int) {
        _uiState.update { it.copy(selectedIndex = index) }
    }

    fun inputNumber(number: Int) {
        val current = _uiState.value
        val selected = current.selectedIndex ?: return
        if (selected in current.givenCells) return
        if (number !in 1..4) return

        moveHistory.add(current.board)
        val updatedBoard = current.board.toMutableList()
        updatedBoard[selected] = number

        _uiState.update {
            it.copy(
                board = updatedBoard,
                isComplete = isBoardFilled(updatedBoard)
            )
        }
    }

    fun startNewGame() {
        val difficulty = _uiState.value.difficulty
        val nextSeedBoard = generatePuzzleBoard(difficulty)
        currentSeedBoard = nextSeedBoard
        moveHistory.clear()
        _uiState.value = MiniSudokuUiState.fromSeed(nextSeedBoard).copy(difficulty = difficulty)
    }

    fun toggleDifficulty() {
        val nextDifficulty = if (_uiState.value.difficulty == MiniSudokuDifficulty.Easy) {
            MiniSudokuDifficulty.Hard
        } else {
            MiniSudokuDifficulty.Easy
        }

        _uiState.update { it.copy(difficulty = nextDifficulty) }
        startNewGame()
    }

    fun restartCurrentGame() {
        moveHistory.clear()
        _uiState.value = MiniSudokuUiState.fromSeed(currentSeedBoard)
            .copy(difficulty = _uiState.value.difficulty)
    }

    fun undoLastMove() {
        val previousBoard = moveHistory.removeLastOrNull() ?: return
        _uiState.update {
            it.copy(
                board = previousBoard,
                isComplete = isBoardFilled(previousBoard)
            )
        }
    }

    private fun generatePuzzleBoard(difficulty: MiniSudokuDifficulty): List<Int?> {
        val solvedBoard = generateSolvedBoard()
        val blanks = if (difficulty == MiniSudokuDifficulty.Easy) 8 else 10
        val puzzleBoard = solvedBoard.toMutableList()
        val indexes = (0 until 16).shuffled(Random).take(blanks)
        indexes.forEach { puzzleBoard[it] = null }
        return puzzleBoard
    }

    private fun generateSolvedBoard(): List<Int?> {
        val baseBoard = IntArray(16) { index ->
            val row = index / 4
            val col = index % 4
            ((row * 2 + row / 2 + col) % 4) + 1
        }

        val rowBands = listOf(0, 1).shuffled(Random)
        val colBands = listOf(0, 1).shuffled(Random)

        val rowOrder = rowBands.flatMap { band ->
            listOf(0, 1).shuffled(Random).map { withinBand -> band * 2 + withinBand }
        }
        val colOrder = colBands.flatMap { band ->
            listOf(0, 1).shuffled(Random).map { withinBand -> band * 2 + withinBand }
        }

        val digitMap = (1..4).shuffled(Random)

        return List(16) { index ->
            val row = rowOrder[index / 4]
            val col = colOrder[index % 4]
            val original = baseBoard[row * 4 + col]
            digitMap[original - 1]
        }
    }

    private fun isBoardFilled(board: List<Int?>): Boolean = board.none { it == null }
}
