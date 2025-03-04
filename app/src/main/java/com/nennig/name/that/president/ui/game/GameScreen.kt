package com.nennig.name.that.president.ui.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.nennig.name.that.president.R
import com.nennig.name.that.president.domain.model.President
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun ModeSelectionScreen(onModeSelected: (Boolean) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Select Game Mode", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onModeSelected(false) }) {
            Text("Endless Mode")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onModeSelected(true) }) {
            Text("Challenge Mode")
        }
    }
}

@Composable
fun GameScreen(
    onGameComplete: () -> Unit,
    viewModel: GameViewModel = hiltViewModel(),
    isChallengeMode: Boolean = false
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showHint by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (uiState) {
            is GameUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is GameUiState.Game -> {
                val state = uiState as GameUiState.Game
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Progress
                    Text(
                        text = "Progress: ${state.currentIndex + 1}/${state.totalPresidents}",
                        style = MaterialTheme.typography.titleLarge
                    )

                    // President Image
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = "file:///android_asset/${state.currentPresident.imageAssetName}"
                        ),
                        contentDescription = "President",
                        modifier = Modifier
                            .size(300.dp)
                            .padding(16.dp)
                    )

                    // Answer Section
                    AnswerSection(
                        currentPresident = state.currentPresident,
                        onAnswerSelected = { president ->
                            viewModel.checkAnswer(president)
                            if (isChallengeMode && viewModel.wrongAnswersCount >= 5) {
                                onGameComplete()
                            }
                        },
                        answerOptions = state.answerOptions
                    )

                    // Hint Button
                    Button(onClick = { showHint = !showHint }) {
                        Text("Show Hint")
                    }

                    // Hint Information
                    if (showHint) {
                        Text(
                            text = "Years in Office: ${state.currentPresident.term}\nParty: ${state.currentPresident.party}",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    // Score
                    Text(
                        text = "Score: ${state.correctAnswers}/${state.currentIndex + 1}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            is GameUiState.Complete -> {
                val state = uiState as GameUiState.Complete
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Game Complete!",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = "Final Score: ${state.correctAnswers}/${state.totalPresidents}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    Button(onClick = onGameComplete) {
                        Text("Back to Main Menu")
                    }
                }
            }
            is GameUiState.Error -> {
                val state = uiState as GameUiState.Error
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AnswerSection(
    currentPresident: President,
    onAnswerSelected: (President) -> Unit,
    answerOptions: List<President>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Who is this president?",
            style = MaterialTheme.typography.titleMedium
        )
        answerOptions.forEach { president ->
            Button(
                onClick = { onAnswerSelected(president) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(president.name)
            }
        }
    }
}

sealed class GameUiState {
    object Loading : GameUiState()
    data class Game(
        val currentPresident: President,
        val currentIndex: Int,
        val totalPresidents: Int,
        val correctAnswers: Int,
        val answerOptions: List<President>
    ) : GameUiState()
    data class Complete(
        val correctAnswers: Int,
        val totalPresidents: Int
    ) : GameUiState()
    data class Error(val message: String) : GameUiState()
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "mode_selection") {
        composable("mode_selection") {
            ModeSelectionScreen { isChallengeMode ->
                navController.navigate("game_screen/$isChallengeMode")
            }
        }
        composable("game_screen/{isChallengeMode}") { backStackEntry ->
            val isChallengeMode = backStackEntry.arguments?.getString("isChallengeMode")?.toBoolean() ?: false
            GameScreen(
                onGameComplete = { navController.popBackStack() },
                isChallengeMode = isChallengeMode
            )
        }
    }
}