package com.binarybricks.iwt.ui.screens.history

data class WorkoutHistoryItem(
    val id: String,
    val date: String,
    val duration: String,
    val steps: String
)

data class WorkoutHistoryUiState(
    val isLoading: Boolean = true,
    val historyItems: List<WorkoutHistoryItem> = emptyList()
)