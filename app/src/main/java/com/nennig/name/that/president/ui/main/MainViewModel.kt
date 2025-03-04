package com.nennig.name.that.president.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nennig.name.that.president.domain.model.President
import com.nennig.name.that.president.domain.repository.PresidentRepository
import com.nennig.name.that.president.domain.repository.Score
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PresidentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _score = MutableStateFlow<Score?>(null)
    val score: StateFlow<Score?> = _score.asStateFlow()

    init {
        loadScore()
    }

    private fun loadScore() {
        viewModelScope.launch {
            repository.getScore().collect { score ->
                _score.value = score
            }
        }
    }

    fun startGame() {
        viewModelScope.launch {
            _uiState.value = MainUiState.GameStarted
        }
    }

    fun resetScore() {
        viewModelScope.launch {
            repository.resetScore()
        }
    }
}

sealed class MainUiState {
    object Loading : MainUiState()
    object GameStarted : MainUiState()
    data class Error(val message: String) : MainUiState()
} 