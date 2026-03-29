package com.cyberfeedforward.emptyactivity.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cyberfeedforward.emptyactivity.ui.state.TicTacToePlayer
import com.cyberfeedforward.emptyactivity.ui.state.TicTacToeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class TicTacToeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TicTacToeUiState())
    val uiState: StateFlow<TicTacToeUiState> = _uiState.asStateFlow()

    fun setBoardSize(size: Int) {
        val normalized = size.coerceIn(3, 10)
        val vsComputer = _uiState.value.vsComputer
        _uiState.value = TicTacToeUiState(
            boardSize = normalized,
            board = List(normalized * normalized) { null },
            currentPlayer = TicTacToePlayer.X,
            vsComputer = vsComputer
        )
    }

    fun restartGame() {
        val size = _uiState.value.boardSize
        val vsComputer = _uiState.value.vsComputer
        _uiState.value = TicTacToeUiState(
            boardSize = size,
            board = List(size * size) { null },
            currentPlayer = TicTacToePlayer.X,
            vsComputer = vsComputer
        )
    }

    fun setVsComputer(enabled: Boolean) {
        val current = _uiState.value
        if (current.vsComputer == enabled) return

        _uiState.value = TicTacToeUiState(
            boardSize = current.boardSize,
            board = List(current.boardSize * current.boardSize) { null },
            currentPlayer = TicTacToePlayer.X,
            vsComputer = enabled
        )
    }

    fun onCellPressed(index: Int) {
        val current = _uiState.value
        if (current.winner != null || current.isDraw) return
        if (index !in current.board.indices) return
        if (current.board[index] != null) return

        if (current.vsComputer && current.currentPlayer != TicTacToePlayer.X) return

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

        val nextCurrentPlayer = if (nextWinner != null || nextIsDraw) current.currentPlayer else nextPlayer
        val nextState = current.copy(
            board = nextBoard,
            currentPlayer = nextCurrentPlayer,
            winner = nextWinner,
            isDraw = nextIsDraw
        )
        _uiState.value = nextState

        if (nextState.vsComputer && nextState.currentPlayer == TicTacToePlayer.O && nextState.winner == null && !nextState.isDraw) {
            makeComputerMove()
        }
    }

    private fun makeComputerMove() {
        val current = _uiState.value
        if (!current.vsComputer) return
        if (current.winner != null || current.isDraw) return
        if (current.currentPlayer != TicTacToePlayer.O) return

        val boardSize = current.boardSize
        val board = current.board
        val moveIndex = pickComputerMove(board = board, boardSize = boardSize)
        if (moveIndex == null) return

        val nextBoard = board.toMutableList().also {
            it[moveIndex] = TicTacToePlayer.O
        }

        val nextWinner = findWinner(nextBoard, boardSize)
        val nextIsDraw = nextWinner == null && nextBoard.all { it != null }
        val nextPlayer = TicTacToePlayer.X

        _uiState.value = current.copy(
            board = nextBoard,
            currentPlayer = if (nextWinner != null || nextIsDraw) current.currentPlayer else nextPlayer,
            winner = nextWinner,
            isDraw = nextIsDraw
        )
    }

    private fun pickComputerMove(board: List<TicTacToePlayer?>, boardSize: Int): Int? {
        val empty = board.indices.filter { board[it] == null }
        if (empty.isEmpty()) return null

        findImmediateWinningMove(board = board, boardSize = boardSize, player = TicTacToePlayer.O)?.let {
            return it
        }
        findImmediateWinningMove(board = board, boardSize = boardSize, player = TicTacToePlayer.X)?.let {
            return it
        }

        val centerIndex = if (boardSize % 2 == 1) {
            val mid = boardSize / 2
            mid * boardSize + mid
        } else {
            null
        }
        if (centerIndex != null && board[centerIndex] == null) return centerIndex

        return empty.random(Random)
    }

    private fun findImmediateWinningMove(
        board: List<TicTacToePlayer?>,
        boardSize: Int,
        player: TicTacToePlayer
    ): Int? {
        for (index in board.indices) {
            if (board[index] != null) continue
            val next = board.toMutableList().also { it[index] = player }
            if (findWinner(next, boardSize) == player) return index
        }
        return null
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
