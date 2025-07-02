package com.binarybricks.iwt.ui.screens.workout

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binarybricks.iwt.data.model.Interval
import com.binarybricks.iwt.data.repository.WorkoutRepository
import com.binarybricks.iwt.services.IwtWorkoutService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val application: Application,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val presetId: String = savedStateHandle.get<String>("presetId")!!

    val uiState: StateFlow<WorkoutUiState> = IwtWorkoutService.serviceState
        .map { serviceState ->
            // Transform service state to UI state
            WorkoutUiState(
                currentTime = formatSeconds(serviceState.intervalTimeLeftSeconds),
                currentIntervalType = serviceState.currentInterval?.type
                    ?: com.binarybricks.iwt.data.model.IntervalType.WARM_UP,
                currentIntervalName = serviceState.currentInterval?.type?.name?.replace("_", " ")
                    ?.lowercase()?.capitalize() ?: "Warm Up",
                stepCount = serviceState.steps,
                totalWorkoutTime = formatSeconds(serviceState.totalTimeElapsedSeconds),
                progress = calculateProgress(serviceState.totalTimeElapsedSeconds),
                isPaused = serviceState.isPaused,
                isFinished = serviceState.isDone
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WorkoutUiState()
        )

    init {
        startWorkoutService()
    }

    private fun startWorkoutService() {
        val preset = workoutRepository.getWorkoutPresets().find { it.id == presetId }
        preset?.let {
            val intent = Intent(application, IwtWorkoutService::class.java).apply {
                action = IwtWorkoutService.ACTION_START
                val bundle = Bundle()
                bundle.putParcelableArrayList(
                    IwtWorkoutService.EXTRA_INTERVALS,
                    ArrayList(it.intervals)
                )
                putExtras(bundle)
            }
            application.startService(intent)
        }
    }

    fun onPauseResume() {
        val action =
            if (uiState.value.isPaused) IwtWorkoutService.ACTION_RESUME else IwtWorkoutService.ACTION_PAUSE
        val intent =
            Intent(application, IwtWorkoutService::class.java).apply { this.action = action }
        application.startService(intent)
    }

    fun onEndWorkout() {
        val intent = Intent(application, IwtWorkoutService::class.java).apply {
            action = IwtWorkoutService.ACTION_STOP
        }
        application.startService(intent)
        // Navigation to summary screen will be triggered by observing isFinished state in the UI
    }

    private fun calculateProgress(elapsedSeconds: Int): Float {
        val preset = workoutRepository.getWorkoutPresets().find { it.id == presetId }
        val totalDuration = preset?.totalDurationMinutes?.times(60) ?: 1
        return (elapsedSeconds.toFloat() / totalDuration.toFloat()).coerceIn(0f, 1f)
    }

    private fun formatSeconds(seconds: Int): String {
        return String.format(
            "%02d:%02d",
            TimeUnit.SECONDS.toMinutes(seconds.toLong()) % 60,
            TimeUnit.SECONDS.toSeconds(seconds.toLong()) % 60
        )
    }

    // Extension function to capitalize only the first letter
    private fun String.capitalize(): String {
        return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}