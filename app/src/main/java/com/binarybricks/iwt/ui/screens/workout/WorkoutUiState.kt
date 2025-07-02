package com.binarybricks.iwt.ui.screens.workout

import com.binarybricks.iwt.data.model.IntervalType

data class WorkoutUiState(
    val currentTime: String = "00:00",
    val currentIntervalType: IntervalType = IntervalType.WARM_UP,
    val currentIntervalName: String = "Warm Up",
    val stepCount: Int = 0,
    val totalWorkoutTime: String = "00:00",
    val progress: Float = 0f,
    val isPaused: Boolean = false,
    val isFinished: Boolean = false
)