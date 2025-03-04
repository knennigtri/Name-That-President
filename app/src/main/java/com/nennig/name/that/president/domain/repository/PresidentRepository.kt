package com.nennig.name.that.president.domain.repository

import com.nennig.name.that.president.domain.model.President
import kotlinx.coroutines.flow.Flow

interface PresidentRepository {
    suspend fun getPresidents(): List<President>
    suspend fun getPresidentById(id: Int): President?
    suspend fun getRandomPresident(): President
    suspend fun getWrongAnswers(): List<President>
    suspend fun saveWrongAnswer(president: President)
    suspend fun clearWrongAnswers()
    fun getScore(): Flow<Score>
    suspend fun updateScore(correct: Int, total: Int)
    suspend fun resetScore()
}

data class Score(
    val mostCorrect: Int = 0,
    val totalAttempts: Int = 0
) 