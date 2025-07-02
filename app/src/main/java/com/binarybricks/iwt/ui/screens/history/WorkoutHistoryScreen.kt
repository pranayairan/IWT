package com.binarybricks.iwt.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.binarybricks.iwt.ui.preview.PreviewWithNavController
import com.binarybricks.iwt.ui.theme.IWTTheme

@Composable
fun WorkoutHistoryScreenRoute(
    navController: NavController,
    viewModel: WorkoutHistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // You can show a loading indicator here if uiState.isLoading is true
    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        WorkoutHistoryScreen(
            navController = navController,
            items = uiState.historyItems,
            onHistoryItemClicked = { itemId ->
                // Navigate to the summary screen for that specific item
                navController.navigate("workout_summary/$itemId")
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutHistoryScreen(
    navController: NavController,
    items: List<WorkoutHistoryItem>,
    onHistoryItemClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "My Workout History",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // History List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items, key = { it.id }) { item ->
                WorkoutHistoryCard(
                    item = item,
                    modifier = Modifier.clickable { onHistoryItemClicked(item.id) }
                )
            }
        }
    }
}

@Composable
fun WorkoutHistoryCard(item: WorkoutHistoryItem, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.date,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = item.duration,
                fontSize = 14.sp,
                color = Color(0xFF4CAF50) // Green color for duration
            )
        }

        Text(
            text = item.steps,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutHistoryScreenPreview() {
    IWTTheme {
        PreviewWithNavController {
            WorkoutHistoryScreen(
                navController = it,
                items = listOf(
                    WorkoutHistoryItem(
                        id = "1",
                        date = "June 28, 2025",
                        duration = "Total Duration: 30 minutes",
                        steps = "Total Steps: 5000"
                    ),
                    WorkoutHistoryItem(
                        id = "2",
                        date = "June 27, 2025",
                        duration = "Total Duration: 45 minutes",
                        steps = "Total Steps: 7500"
                    )
                ),
                onHistoryItemClicked = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutHistoryCardPreview() {
    IWTTheme {
        WorkoutHistoryCard(
            item = WorkoutHistoryItem(
                id = "1",
                date = "June 28, 2025",
                duration = "Total Duration: 30 minutes",
                steps = "Total Steps: 5000"
            )
        )
    }
}