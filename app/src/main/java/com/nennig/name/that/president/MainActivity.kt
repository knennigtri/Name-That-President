package com.nennig.name.that.president

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nennig.name.that.president.ui.game.GameScreen
import com.nennig.name.that.president.ui.main.MainScreen
import com.nennig.name.that.president.ui.review.ReviewScreen
import com.nennig.name.that.president.ui.theme.PresidentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PresidentTheme {
                PresidentApp()
            }
        }
    }
}

@Composable
fun PresidentApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                onStartGame = { navController.navigate("game") },
                onReviewMode = { navController.navigate("review") },
                onMoreGames = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/search?q=Name+That&c=apps&price=1")
                    )
                    // TODO: Launch intent
                }
            )
        }
        composable("game") {
            GameScreen(
                onGameComplete = { navController.navigate("main") }
            )
        }
        composable("review") {
            ReviewScreen(
                onBackToMain = { navController.navigate("main") }
            )
        }
    }
} 