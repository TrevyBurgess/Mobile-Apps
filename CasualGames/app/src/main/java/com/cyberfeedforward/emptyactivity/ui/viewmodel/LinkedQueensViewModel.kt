package com.cyberfeedforward.emptyactivity.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyberfeedforward.emptyactivity.ui.state.LinkedQueensDifficulty
import com.cyberfeedforward.emptyactivity.ui.state.LinkedQueensUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random

class LinkedQueensViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LinkedQueensUiState())
    val uiState: StateFlow<LinkedQueensUiState> = _uiState.asStateFlow()

    private val moveHistory = mutableListOf<Set<Int>>()
    private var currentRegions: List<Int> = List(25) { 0 }
    private val seenPuzzleLayoutsBySize = mutableMapOf<Int, MutableSet<String>>()
    private var generationJob: Job? = null

    init {
        startNewGame()
    }

    fun toggleCell(index: Int) {
        val current = _uiState.value
        val size = current.boardSize
        if (index !in 0 until size * size) return

        moveHistory.add(current.queens)

        val row = index / size
        val nextQueens = current.queens
            .filterNot { it / size == row }
            .toMutableSet()

        if (index !in current.queens) {
            nextQueens.add(index)
        }

        _uiState.update {
            it.copy(
                queens = nextQueens,
                isComplete = isSolved(size, nextQueens, it.regions)
            )
        }
    }

    fun toggleHints() {
        _uiState.update { it.copy(hintsEnabled = !it.hintsEnabled) }
    }

    fun startNewGame() {
        val hintsEnabled = _uiState.value.hintsEnabled
        val difficulty = _uiState.value.difficulty
        generationJob?.cancel()
        _uiState.update { it.copy(isGenerating = true, isComplete = false, queens = emptySet()) }

        generationJob = viewModelScope.launch(Dispatchers.Default) {
            val size = if (difficulty == LinkedQueensDifficulty.Easy) 5 else 7
            val regions = generateUniqueRegions(size)

            withContext(Dispatchers.Main) {
                currentRegions = regions
                moveHistory.clear()

                _uiState.value = LinkedQueensUiState(
                    boardSize = size,
                    regions = regions,
                    queens = emptySet(),
                    isGenerating = false,
                    isComplete = false,
                    hintsEnabled = hintsEnabled,
                    difficulty = difficulty
                )
            }
        }
    }

    fun toggleDifficulty() {
        val nextDifficulty = if (_uiState.value.difficulty == LinkedQueensDifficulty.Easy) {
            LinkedQueensDifficulty.Hard
        } else {
            LinkedQueensDifficulty.Easy
        }

        _uiState.update { it.copy(difficulty = nextDifficulty) }
        startNewGame()
    }

    fun restartCurrentGame() {
        if (_uiState.value.isGenerating) return
        moveHistory.clear()
        _uiState.value = _uiState.value.copy(
            queens = emptySet(),
            regions = currentRegions,
            isComplete = false
        )
    }

    fun undoLastMove() {
        if (_uiState.value.isGenerating) return
        val previous = moveHistory.removeLastOrNull() ?: return
        _uiState.update {
            it.copy(
                queens = previous,
                isComplete = isSolved(it.boardSize, previous, it.regions)
            )
        }
    }

    private fun generateSolution(size: Int): IntArray {
        while (true) {
            val columns = IntArray(size) { -1 }
            if (solveRow(0, size, columns)) {
                return columns
            }
        }
    }

    private fun generateUniqueRegions(size: Int): List<Int> {
        val seenForSize = seenPuzzleLayoutsBySize.getOrPut(size) { mutableSetOf() }

        while (true) {
            val candidate = generateRegions(generateSolution(size), size)
            val key = candidate.joinToString(",")
            if (key in seenForSize) continue
            if (!hasExactlyOneSolution(size, candidate)) continue

            seenForSize.add(key)
            return candidate
        }
    }

    private fun solveRow(row: Int, size: Int, columns: IntArray): Boolean {
        if (row == size) return true

        val candidates = (0 until size).shuffled(Random)
        for (col in candidates) {
            if (isSafePlacement(row, col, columns)) {
                columns[row] = col
                if (solveRow(row + 1, size, columns)) return true
                columns[row] = -1
            }
        }

        return false
    }

    private fun isSafePlacement(row: Int, col: Int, columns: IntArray): Boolean {
        for (r in 0 until row) {
            val c = columns[r]
            if (c == col) return false
            if (abs(c - col) == abs(r - row)) return false
        }
        return true
    }

    private fun generateRegions(solution: IntArray, size: Int): List<Int> {
        val seeds = solution.mapIndexed { row, col -> row to col }

        return List(size * size) { index ->
            val row = index / size
            val col = index % size
            var bestRegion = 0
            var bestScore = Int.MAX_VALUE

            seeds.forEachIndexed { regionId, (seedRow, seedCol) ->
                val manhattan = abs(row - seedRow) + abs(col - seedCol)
                val tieBreaker = max(abs(row - seedRow), abs(col - seedCol))
                val score = manhattan * 10 + tieBreaker + Random.nextInt(0, 3)
                if (score < bestScore) {
                    bestScore = score
                    bestRegion = regionId
                }
            }

            bestRegion
        }
    }

    private fun isSolved(size: Int, queens: Set<Int>, regions: List<Int>): Boolean {
        if (queens.size != size) return false

        val rows = mutableSetOf<Int>()
        val cols = mutableSetOf<Int>()
        val regionHits = mutableSetOf<Int>()

        queens.forEach { index ->
            val row = index / size
            val col = index % size

            if (!rows.add(row)) return false
            if (!cols.add(col)) return false
            if (!regionHits.add(regions[index])) return false
        }

        val queenList = queens.toList()
        for (i in queenList.indices) {
            for (j in i + 1 until queenList.size) {
                val a = queenList[i]
                val b = queenList[j]
                val rowDiff = abs(a / size - b / size)
                val colDiff = abs(a % size - b % size)
                if (rowDiff == colDiff) return false
            }
        }

        return true
    }

    private fun hasExactlyOneSolution(size: Int, regions: List<Int>): Boolean {
        val usedCols = BooleanArray(size)
        val usedDiagDown = HashSet<Int>()
        val usedDiagUp = HashSet<Int>()
        val usedRegions = HashSet<Int>()

        fun count(row: Int, found: Int): Int {
            if (found > 1) return found
            if (row == size) return found + 1

            var solutions = found
            for (col in 0 until size) {
                val down = row - col
                val up = row + col
                val region = regions[row * size + col]

                if (usedCols[col]) continue
                if (down in usedDiagDown || up in usedDiagUp) continue
                if (region in usedRegions) continue

                usedCols[col] = true
                usedDiagDown.add(down)
                usedDiagUp.add(up)
                usedRegions.add(region)

                solutions = count(row + 1, solutions)

                usedCols[col] = false
                usedDiagDown.remove(down)
                usedDiagUp.remove(up)
                usedRegions.remove(region)

                if (solutions > 1) return solutions
            }

            return solutions
        }

        return count(row = 0, found = 0) == 1
    }
}
