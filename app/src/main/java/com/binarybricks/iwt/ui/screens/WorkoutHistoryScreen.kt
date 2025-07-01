package com.binarybricks.iwt.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binarybricks.iwt.R
import com.binarybricks.iwt.ui.theme.IWTTheme

data class WorkoutHistoryItem(
    val date: String,
    val duration: String,
    val steps: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutHistoryScreen() {
    val historyItems = listOf(
        WorkoutHistoryItem("June 28, 2025", "Total Duration: 30 minutes", "Total Steps: 5000"),
        WorkoutHistoryItem("June 27, 2025", "Total Duration: 45 minutes", "Total Steps: 7500"),
        WorkoutHistoryItem("June 26, 2025", "Total Duration: 20 minutes", "Total Steps: 3000"),
        WorkoutHistoryItem("June 25, 2025", "Total Duration: 60 minutes", "Total Steps: 10000"),
        WorkoutHistoryItem("June 24, 2025", "Total Duration: 35 minutes", "Total Steps: 6000"),
        WorkoutHistoryItem("June 23, 2025", "Total Duration: 50 minutes", "Total Steps: 8500"),
        WorkoutHistoryItem("June 22, 2025", "Total Duration: 25 minutes", "Total Steps: 4000"),
        WorkoutHistoryItem("June 21, 2025", "Total Duration: 40 minutes", "Total Steps: 7000"),
        WorkoutHistoryItem("June 20, 2025", "Total Duration: 55 minutes", "Total Steps: 9500"),
        WorkoutHistoryItem("June 19, 2025", "Total Duration: 30 minutes", "Total Steps: 5000")
    )

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
                IconButton(onClick = { /* TODO: Handle back navigation */ }) {
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
            items(historyItems) { item ->
                WorkoutHistoryCard(item = item)
            }
        }
    }
}

@Composable
fun WorkoutHistoryCard(item: WorkoutHistoryItem) {
    Row(
        modifier = Modifier
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
        WorkoutHistoryScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutHistoryCardPreview() {
    IWTTheme {
        WorkoutHistoryCard(
            item = WorkoutHistoryItem(
                "June 28, 2025",
                "Total Duration: 30 minutes",
                "Total Steps: 5000"
            )
        )
    }
}