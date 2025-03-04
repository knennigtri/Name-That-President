package com.nennig.name.that.president.ui.review

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
class ReviewViewModel @Inject constructor(
    private val repository: PresidentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReviewUiState>(ReviewUiState.Loading)
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    init {
        loadPresidents()
    }

    private fun loadPresidents() {
        viewModelScope.launch {
            try {
                val presidents = repository.getPresidents()
                if (presidents.isEmpty()) {
                    _uiState.value = ReviewUiState.Error("No presidents found")
                    return@launch
                }
                _uiState.value = ReviewUiState.Review(presidents)
            } catch (e: Exception) {
                _uiState.value = ReviewUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
} 