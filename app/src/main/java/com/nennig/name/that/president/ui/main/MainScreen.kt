package com.nennig.name.that.president.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nennig.name.that.president.R
import com.nennig.name.that.president.domain.repository.Score

@Composable
fun MainScreen(
    onStartGame: () -> Unit,
    onReviewMode: () -> Unit,
    onMoreGames: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val score by viewModel.score.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Title
            Text(
                text = "Name That President",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 32.dp)
            )

            // Main Image
            Image(
                painter = painterResource(id = R.drawable.abraham_lincoln),
                contentDescription = "Abraham Lincoln",
                modifier = Modifier
                    .size(200.dp)
                    .padding(16.dp)
            )

            // Score Section
            ScoreSection(score = score)

            // Buttons
            ButtonSection(
                onStartGame = onStartGame,
                onReviewMode = onReviewMode,
                onMoreGames = onMoreGames
            )
        }
    }
}

@Composable
private fun ScoreSection(score: Score?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Best Attempt: ${score?.mostCorrect ?: 0}/45",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Total Attempts: ${score?.totalAttempts ?: 0}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun ButtonSection(
    onStartGame: () -> Unit,
    onReviewMode: () -> Unit,
    onMoreGames: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        Button(
            onClick = onStartGame,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Game")
        }

        Button(
            onClick = onReviewMode,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Review Mode")
        }

        Button(
            onClick = onMoreGames,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("More Games")
        }
    }
} 