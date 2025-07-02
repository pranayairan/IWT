package com.binarybricks.iwt.ui.screens.summary

data class WorkoutSummaryUiState(
    val isLoading: Boolean = true,
    val totalDuration: String = "",
    val totalSteps: String = "",
    val fastWalkTime: String = "",
    val slowWalkTime: String = ""
)