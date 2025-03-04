package com.nennig.name.that.president.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nennig.name.that.president.domain.model.President
import com.nennig.name.that.president.domain.repository.PresidentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: PresidentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameUiState>(GameUiState.Loading)
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var presidents: List<President> = emptyList()
    private var currentIndex = 0
    private var correctAnswers = 0

    init {
        loadPresidents()
    }

    private fun loadPresidents() {
        viewModelScope.launch {
            try {
                presidents = repository.getPresidents().shuffled()
                if (presidents.isEmpty()) {
                    _uiState.value = GameUiState.Error("No presidents found")
                    return@launch
                }
                updateGameState()
            } catch (e: Exception) {
                _uiState.value = GameUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun updateGameState() {
        if (currentIndex >= presidents.size) {
            _uiState.value = GameUiState.Complete(
                correctAnswers = correctAnswers,
                totalPresidents = presidents.size
            )
            return
        }

        val currentPresident = presidents[currentIndex]
        val answerOptions = generateAnswerOptions(currentPresident)
        
        _uiState.value = GameUiState.Game(
            currentPresident = currentPresident,
            currentIndex = currentIndex,
            totalPresidents = presidents.size,
            correctAnswers = correctAnswers,
            answerOptions = answerOptions
        )
    }

    private fun generateAnswerOptions(correctPresident: President): List<President> {
        val options = mutableListOf(correctPresident)
        val otherPresidents = presidents.filter { it != correctPresident }.shuffled()
        
        // Add 3 random incorrect presidents
        options.addAll(otherPresidents.take(3))
        
        // Shuffle the options
        return options.shuffled()
    }

    fun checkAnswer(selectedPresident: President) {
        val currentPresident = presidents[currentIndex]
        if (selectedPresident.id == currentPresident.id) {
            correctAnswers++
        } else {
            viewModelScope.launch {
                repository.saveWrongAnswer(currentPresident)
            }
        }
        currentIndex++
        updateGameState()
    }
} 