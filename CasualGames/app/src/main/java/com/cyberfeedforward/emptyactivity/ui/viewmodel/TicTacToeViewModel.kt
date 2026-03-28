package com.cyberfeedforward.emptyactivity.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cyberfeedforward.emptyactivity.ui.state.TicTacToePlayer
import com.cyberfeedforward.emptyactivity.ui.state.TicTacToeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TicTacToeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TicTacToeUiState())
    val uiState: StateFlow<TicTacToeUiState> = _uiState.asStateFlow()

    fun setBoardSize(size: Int) {
        val normalized = size.coerceIn(3, 10)
        _uiState.value = TicTacToeUiState(
            boardSize = normalized,
            board = List(normalized * normalized) { null }
        )
    }

    fun restartGame() {
        val size = _uiState.value.boardSize
        _uiState.value = TicTacToeUiState(
            boardSize = size,
            board = List(size * size) { null }
        )
    }

    fun onCellPressed(index: Int) {
        val current = _uiState.value
        if (current.winner != null || current.isDraw) return
        if (index !in current.board.indices) return
        if (current.board[index] != null) return

        val nextBoard = current.board.toMutableList().also {
            it[index] = current.currentPlayer
        }

        val nextWinner = findWinner(nextBoard, current.boardSize)
        val nextIsDraw = nextWinner == null && nextBoard.all { it != null }
        val nextPlayer = if (current.currentPlayer == TicTacToePlayer.X) {
            TicTacToePlayer.O
        } else {
            TicTacToePlayer.X
        }

        _uiState.update {
            it.copy(
                board = nextBoard,
                currentPlayer = if (nextWinner != null || nextIsDraw) it.currentPlayer else nextPlayer,
                winner = nextWinner,
                isDraw = nextIsDraw
            )
        }
    }

    private fun findWinner(board: List<TicTacToePlayer?>, size: Int): TicTacToePlayer? {
        fun cell(row: Int, col: Int): TicTacToePlayer? = board[row * size + col]

        for (row in 0 until size) {
            val first = cell(row, 0) ?: continue
            var all = true
            for (col in 1 until size) {
                if (cell(row, col) != first) {
                    all = false
                    break
                }
            }
            if (all) return first
        }

        for (col in 0 until size) {
            val first = cell(0, col) ?: continue
            var all = true
            for (row in 1 until size) {
                if (cell(row, col) != first) {
                    all = false
                    break
                }
            }
            if (all) return first
        }

        run {
            val first = cell(0, 0) ?: return@run
            var all = true
            for (i in 1 until size) {
                if (cell(i, i) != first) {
                    all = false
                    break
                }
            }
            if (all) return first
        }

        run {
            val first = cell(0, size - 1) ?: return@run
            var all = true
            for (i in 1 until size) {
                if (cell(i, size - 1 - i) != first) {
                    all = false
                    break
                }
            }
            if (all) return first
        }

        return null
    }
}
