package com.nennig.name.that.president.ui.review

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.nennig.name.that.president.domain.model.President

@Composable
fun ReviewScreen(
    onBackToMain: () -> Unit,
    viewModel: ReviewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (uiState) {
            is ReviewUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is ReviewUiState.Review -> {
                val state = uiState as ReviewUiState.Review
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header
                    Text(
                        text = "Review Presidents",
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(16.dp)
                    )

                    // President List
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.presidents) { president ->
                            PresidentCard(president = president)
                        }
                    }

                    // Back Button
                    Button(
                        onClick = onBackToMain,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Back to Main Menu")
                    }
                }
            }
            is ReviewUiState.Error -> {
                val state = uiState as ReviewUiState.Error
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
private fun PresidentCard(president: President) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = "file:///android_asset/${president.imageAssetName}"
                ),
                contentDescription = president.name,
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
            )
            Text(
                text = president.name,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = president.term,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = president.party,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

sealed class ReviewUiState {
    object Loading : ReviewUiState()
    data class Review(val presidents: List<President>) : ReviewUiState()
    data class Error(val message: String) : ReviewUiState()
} 