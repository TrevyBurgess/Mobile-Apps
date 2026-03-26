package com.cyberfeedforward.emptyactivity.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cyberfeedforward.emptyactivity.ui.state.WordleGameStatus
import com.cyberfeedforward.emptyactivity.ui.state.WordleLetterState
import com.cyberfeedforward.emptyactivity.ui.state.WordleUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class WordleViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(WordleUiState())
    val uiState: StateFlow<WordleUiState> = _uiState.asStateFlow()

    private val wordList: List<String> = listOf(
        "ABOUT",
        "ABOVE",
        "ACTOR",
        "ADULT",
        "AFTER",
        "AGAIN",
        "ALONE",
        "APPLE",
        "BEACH",
        "BRAIN",
        "BREAD",
        "BRICK",
        "BRING",
        "BUILD",
        "CHAIR",
        "CRANE",
        "DANCE",
        "DRIVE",
        "EARTH",
        "FAITH",
        "GAMES",
        "GRAPE",
        "HEART",
        "HOUSE",
        "INDEX",
        "JELLY",
        "KNIFE",
        "LIGHT",
        "MONEY",
        "NURSE",
        "OCEAN",
        "PIZZA",
        "QUEEN",
        "RIVER",
        "SMILE",
        "TRAIN",
        "UNITY",
        "VIRUS",
        "WATER",
        "WORLD"
    )

    private val maxGuesses: Int = 6
    private val wordLength: Int = 5

    init {
        startNewGame()
    }

    fun startNewGame() {
        val secret = wordList.random(Random)
        _uiState.value = WordleUiState(secret = secret)
    }

    fun restartCurrentGame() {
        val secret = _uiState.value.secret
        _uiState.value = WordleUiState(secret = secret)
    }

    fun inputLetter(letter: Char) {
        val current = _uiState.value
        if (current.status != WordleGameStatus.InProgress) return

        val upper = letter.uppercaseChar()
        if (upper !in 'A'..'Z') return

        if (current.currentGuess.length >= wordLength) return

        _uiState.update { it.copy(currentGuess = it.currentGuess + upper) }
    }

    fun backspace() {
        val current = _uiState.value
        if (current.status != WordleGameStatus.InProgress) return
        if (current.currentGuess.isEmpty()) return

        _uiState.update { it.copy(currentGuess = it.currentGuess.dropLast(1)) }
    }

    fun submitGuess() {
        val current = _uiState.value
        if (current.status != WordleGameStatus.InProgress) return
        if (current.currentGuess.length != wordLength) return
        if (current.guesses.size >= maxGuesses) return

        val guess = current.currentGuess
        val evaluation = evaluateGuess(secret = current.secret, guess = guess)
        val nextGuesses = current.guesses + guess
        val nextEvaluations = current.lastEvaluations + listOf(evaluation)

        val nextStatus = when {
            guess == current.secret -> WordleGameStatus.Won
            nextGuesses.size >= maxGuesses -> WordleGameStatus.Lost
            else -> WordleGameStatus.InProgress
        }

        _uiState.value = current.copy(
            guesses = nextGuesses,
            currentGuess = "",
            status = nextStatus,
            lastEvaluations = nextEvaluations
        )
    }

    private fun evaluateGuess(secret: String, guess: String): List<WordleLetterState> {
        val result = MutableList(wordLength) { WordleLetterState.Absent }
        val secretChars = secret.toCharArray()
        val used = BooleanArray(wordLength)

        for (i in 0 until wordLength) {
            if (guess[i] == secretChars[i]) {
                result[i] = WordleLetterState.Correct
                used[i] = true
            }
        }

        for (i in 0 until wordLength) {
            if (result[i] == WordleLetterState.Correct) continue

            val ch = guess[i]
            val foundIndex = (0 until wordLength).firstOrNull { j -> !used[j] && secretChars[j] == ch }
            if (foundIndex != null) {
                result[i] = WordleLetterState.Present
                used[foundIndex] = true
            }
        }

        return result
    }
}
