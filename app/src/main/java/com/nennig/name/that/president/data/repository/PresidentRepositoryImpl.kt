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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.coroutines.flow.flowOf

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "president_game")

class PresidentRepositoryImpl(private val context: Context) : PresidentRepository {
    private val wrongAnswers = mutableListOf<President>()
    private var mostCorrect = 0
    private var totalAttempts = 0

    override suspend fun getPresidents(): List<President> {
        return PresidentDataSource.presidents
    }

    override suspend fun getPresidentById(id: Int): President? {
        return getPresidents().find { president: President -> president.id == id }
    }

    override suspend fun getRandomPresident(): President {
        val presidents = getPresidents()
        return presidents.random()
    }

    override suspend fun getWrongAnswers(): List<President> {
        return wrongAnswers
    }

    override suspend fun saveWrongAnswer(president: President) {
        if (!wrongAnswers.contains(president)) {
            wrongAnswers.add(president)
        }
    }

    override suspend fun clearWrongAnswers() {
        wrongAnswers.clear()
    }

    override fun getScore(): Flow<Score> {
        return flowOf(Score(mostCorrect, totalAttempts))
    }

    override suspend fun updateScore(correct: Int, total: Int) {
        mostCorrect = maxOf(mostCorrect, correct)
        totalAttempts += total
    }

    override suspend fun resetScore() {
        mostCorrect = 0
        totalAttempts = 0
        wrongAnswers.clear()
    }
} 