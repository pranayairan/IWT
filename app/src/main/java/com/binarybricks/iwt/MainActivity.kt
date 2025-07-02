package com.binarybricks.iwt

import HomeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.binarybricks.iwt.ui.screens.SettingsScreen
import com.binarybricks.iwt.ui.screens.WorkoutHistoryScreen
import com.binarybricks.iwt.ui.screens.workout.WorkoutScreenRoute
import com.binarybricks.iwt.ui.screens.WorkoutSummaryScreen
import com.binarybricks.iwt.ui.screens.WorkoutSummaryScreenRoute
import com.binarybricks.iwt.ui.theme.IWTTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IWTTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavHost(navController = navController)
                }
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("settings") {
            SettingsScreen(navController = navController)
        }
        composable("history") {
            WorkoutHistoryScreen(navController = navController)
        }
        composable(
            route = "workout/{presetId}",
            arguments = listOf(
                navArgument("presetId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val presetId = backStackEntry.arguments?.getString("presetId") ?: ""
            WorkoutScreenRoute(
                navController = navController,
                presetId = presetId
            )
        }
        composable(
            route = "workout_summary/{workoutId}",
            arguments = listOf(
                navArgument("workoutId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId") ?: ""
            WorkoutSummaryScreenRoute(
                navController = navController,
                workoutId = workoutId
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    IWTTheme {
        val navController = rememberNavController()
        AppNavHost(navController = navController)
    }
}