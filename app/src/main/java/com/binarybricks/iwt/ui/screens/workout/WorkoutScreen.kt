package com.binarybricks.iwt.ui.screens.workout

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.binarybricks.iwt.ui.preview.PreviewWithNavController
import com.binarybricks.iwt.ui.theme.IWTTheme
import androidx.compose.material3.ExperimentalMaterial3Api

@Composable
fun WorkoutScreenRoute(
    navController: NavController,
    presetId: String,
    viewModel: WorkoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Keep screen on during workout only if enabled in settings
    if (uiState.keepScreenOnEnabled) {
        KeepScreenOn()
    }

    // Listen for navigation events from the ViewModel
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { newLogId ->
            navController.navigate("workout_summary/$newLogId") {
                // Pop up to the home screen to clear the workout from the back stack
                popUpTo("home")
            }
        }
    }

    // Pass state down to your existing stateless composable
    WorkoutScreen(
        navController = navController,
        presetId = presetId,
        currentTime = uiState.currentTime,
        currentInterval = uiState.currentIntervalName,
        stepCount = uiState.stepCount,
        totalWorkoutTime = uiState.totalWorkoutTime,
        progress = uiState.progress,
        isPaused = uiState.isPaused,
        onPauseResume = viewModel::onPauseResume,
        onEndWorkout = viewModel::onEndWorkout
    )
}

@Composable
fun KeepScreenOn() {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val window = (context as? Activity)?.window
        window?.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            window?.clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun WorkoutScreen(
    navController: NavController,
    presetId: String,
    currentTime: String = "02:45",
    currentInterval: String = "Fast Walk",
    stepCount: Int = 1234,
    totalWorkoutTime: String = "10:45",
    progress: Float = 0.6f,
    isPaused: Boolean = false,
    onPauseResume: () -> Unit = {},
    onEndWorkout: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = com.binarybricks.iwt.R.drawable.vector_0_2),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Current interval and time at top
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currentInterval,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Text(
                    text = currentTime,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
            }

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(8.dp)
                    .background(Color(0xFFE0E0E0), RoundedCornerShape(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .background(Color(0xFF4CAF50), RoundedCornerShape(4.dp))
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Central display with steps
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Steps icon and count
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = com.binarybricks.iwt.R.drawable.vector_1_0),
                        contentDescription = "Steps",
                        tint = Color(0xFF333333),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stepCount.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom control buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Pause button
                Button(
                    onClick = onPauseResume,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE0E0E0)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (isPaused) "Resume" else "Pause",
                        fontSize = 16.sp,
                        color = Color(0xFF333333),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // End button
                Button(
                    onClick = onEndWorkout,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE0E0E0)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "End",
                        fontSize = 16.sp,
                        color = Color(0xFF333333),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutScreenPreview() {
    PreviewWithNavController {
        IWTTheme {
            WorkoutScreenRoute(navController = it, presetId = "")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutScreenFastWalkPreview() {
    PreviewWithNavController {
        IWTTheme {
            WorkoutScreenRoute(
                navController = it,
                presetId = "",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutScreenPausedPreview() {
    PreviewWithNavController {
        IWTTheme {
            WorkoutScreenRoute(
                navController = it,
                presetId = "",
            )
        }
    }
}