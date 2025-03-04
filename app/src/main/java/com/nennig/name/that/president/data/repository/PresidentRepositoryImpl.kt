package com.nennig.name.that.president.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nennig.name.that.president.data.source.PresidentDataSource
import com.nennig.name.that.president.domain.model.President
import com.nennig.name.that.president.domain.repository.PresidentRepository
import com.nennig.name.that.president.domain.repository.Score
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "president_game")

class PresidentRepositoryImpl(private val context: Context) : PresidentRepository {
    private val mostCorrectKey = intPreferencesKey("most_correct")
    private val totalAttemptsKey = intPreferencesKey("total_attempts")
    private val wrongAnswersKey = stringPreferencesKey("wrong_answers")

    override suspend fun getPresidents(): List<President> {
        return PresidentDataSource.presidents
    }

    override suspend fun getPresidentById(id: Int): President? {
        return getPresidents().find { it.id == id }
    }

    override suspend fun getRandomPresident(): President {
        val presidents = getPresidents()
        return presidents.random()
    }

    override suspend fun getWrongAnswers(): List<President> {
        val wrongAnswersJson = context.dataStore.data.map { preferences ->
            preferences[wrongAnswersKey] ?: "[]"
        }
        return try {
            Json.decodeFromString(wrongAnswersJson)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun saveWrongAnswer(president: President) {
        val wrongAnswers = getWrongAnswers().toMutableList()
        if (!wrongAnswers.contains(president)) {
            wrongAnswers.add(president)
            context.dataStore.edit { preferences ->
                preferences[wrongAnswersKey] = Json.encodeToString(wrongAnswers)
            }
        }
    }

    override suspend fun clearWrongAnswers() {
        context.dataStore.edit { preferences ->
            preferences.remove(wrongAnswersKey)
        }
    }

    override fun getScore(): Flow<Score> {
        return context.dataStore.data.map { preferences ->
            Score(
                mostCorrect = preferences[mostCorrectKey] ?: 0,
                totalAttempts = preferences[totalAttemptsKey] ?: 0
            )
        }
    }

    override suspend fun updateScore(correct: Int, total: Int) {
        context.dataStore.edit { preferences ->
            val currentMostCorrect = preferences[mostCorrectKey] ?: 0
            val currentTotalAttempts = preferences[totalAttemptsKey] ?: 0
            
            preferences[mostCorrectKey] = maxOf(currentMostCorrect, correct)
            preferences[totalAttemptsKey] = currentTotalAttempts + total
        }
    }

    override suspend fun resetScore() {
        context.dataStore.edit { preferences ->
            preferences.remove(mostCorrectKey)
            preferences.remove(totalAttemptsKey)
            preferences.remove(wrongAnswersKey)
        }
    }
} 