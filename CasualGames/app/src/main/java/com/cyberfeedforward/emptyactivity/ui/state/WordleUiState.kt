package com.cyberfeedforward.emptyactivity.ui.state

enum class WordleGameStatus {
    InProgress,
    Won,
    Lost
}

enum class WordleLetterState {
    Absent,
    Present,
    Correct
}

data class WordleUiState(
    val secret: String = "",
    val guesses: List<String> = emptyList(),
    val currentGuess: String = "",
    val status: WordleGameStatus = WordleGameStatus.InProgress,
    val lastEvaluations: List<List<WordleLetterState>> = emptyList()
)
