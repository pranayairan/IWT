package com.binarybricks.iwt.ui.screens.summary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.binarybricks.iwt.ui.preview.PreviewWithNavController
import com.binarybricks.iwt.ui.theme.IWTTheme

@Composable
fun WorkoutSummaryScreenRoute(
    navController: NavController,
    workoutId: String,
    viewModel: WorkoutSummaryViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    fun onDoneClicked() {
        navController.navigate("home") {
            popUpTo("home") {
                inclusive = true
            }
        }
    }

    // You can add a loading indicator here if uiState.isLoading is true
    if (!uiState.value.isLoading) {
        WorkoutSummaryScreen(
            navController = navController,
            workoutId = workoutId,
            totalDuration = uiState.value.totalDuration,
            totalSteps = uiState.value.totalSteps,
            fastWalkTime = uiState.value.fastWalkTime,
            slowWalkTime = uiState.value.slowWalkTime,
            onDone = ::onDoneClicked
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSummaryScreen(
    navController: NavController,
    workoutId: String,
    totalDuration: String = "30:00 minutes",
    totalSteps: String = "3,210 steps",
    fastWalkTime: String = "15:00 minutes",
    slowWalkTime: String = "15:00 minutes",
    onDone: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout Summary") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = com.binarybricks.iwt.R.drawable.vector_icon),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8FAF8))
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Cat and plant illustration
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = com.binarybricks.iwt.R.drawable.cat_plant_icon),
                    contentDescription = "Cat with plant",
                    modifier = Modifier.size(200.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Workout Complete title
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Workout Complete!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D1C0D)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Stats section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                // First row: Total Duration and Total Steps
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Total Duration
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Total\nDuration",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4CAF50)
                        )
                        Text(
                            text = totalDuration,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D1C0D)
                        )
                    }

                    // Total Steps
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Total Steps",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4CAF50)
                        )
                        Text(
                            text = totalSteps,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D1C0D)
                        )
                    }
                }

                // Second row: Fast Walk Time and Slow Walk Time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Fast Walk Time
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Fast Walk",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4CAF50)
                        )
                        Text(
                            text = fastWalkTime,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D1C0D)
                        )
                    }

                    // Slow Walk Time
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Slow Walk",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4CAF50)
                        )
                        Text(
                            text = slowWalkTime,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D1C0D)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Done button at bottom
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Button(
                    onClick = onDone,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "Done",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutSummaryScreenPreview() {
    PreviewWithNavController {
        IWTTheme {
            WorkoutSummaryScreenRoute(navController = it, workoutId = "")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutSummaryScreenCustomPreview() {
    PreviewWithNavController {
        IWTTheme {
            WorkoutSummaryScreenRoute(
                navController = it,
                workoutId = "",
            )
        }
    }
}