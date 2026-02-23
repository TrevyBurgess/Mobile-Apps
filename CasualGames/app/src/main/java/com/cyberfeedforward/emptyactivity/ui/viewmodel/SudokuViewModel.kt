package com.cyberfeedforward.emptyactivity.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cyberfeedforward.emptyactivity.ui.state.SudokuUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class SudokuViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SudokuUiState())
    val uiState: StateFlow<SudokuUiState> = _uiState.asStateFlow()
    private var currentSeedBoard: List<Int?> = SudokuUiState.initialBoard

    fun selectCell(index: Int) {
        _uiState.update { it.copy(selectedIndex = index) }
    }

    fun inputNumber(number: Int) {
        val current = _uiState.value
        val selected = current.selectedIndex ?: return
        if (selected in current.givenCells) return
        if (number !in 1..9) return

        val updatedBoard = current.board.toMutableList()
        updatedBoard[selected] = number

        _uiState.update {
            it.copy(
                board = updatedBoard,
                isComplete = isBoardFilled(updatedBoard)
            )
        }
    }

    fun clearSelected() {
        val current = _uiState.value
        val selected = current.selectedIndex ?: return
        if (selected in current.givenCells) return

        val updatedBoard = current.board.toMutableList()
        updatedBoard[selected] = null

        _uiState.update {
            it.copy(
                board = updatedBoard,
                isComplete = false
            )
        }
    }

    fun startNewGame() {
        val candidates = SudokuUiState.seedBoards.filter { it != currentSeedBoard }
        val nextSeedBoard = if (candidates.isNotEmpty()) {
            candidates.random(Random)
        } else {
            SudokuUiState.seedBoards.random(Random)
        }
        currentSeedBoard = nextSeedBoard
        _uiState.value = SudokuUiState.fromSeed(nextSeedBoard)
    }

    fun restartCurrentGame() {
        _uiState.value = SudokuUiState.fromSeed(currentSeedBoard)
    }

    private fun isBoardFilled(board: List<Int?>): Boolean = board.none { it == null }
}
