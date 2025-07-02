package com.binarybricks.iwt.ui.screens.home

import com.binarybricks.iwt.data.model.WorkoutLog
import com.binarybricks.iwt.data.model.WorkoutPreset

data class HomeUiState(
    val presets: List<WorkoutPreset> = emptyList(),
    val selectedPresetId: String? = null,
    val lastWorkout: WorkoutLog? = null,
    val isActivityPermissionGranted: Boolean = false
)