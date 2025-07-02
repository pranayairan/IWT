package com.binarybricks.iwt.ui.screens.summary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binarybricks.iwt.data.model.WorkoutLog
import com.binarybricks.iwt.data.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class WorkoutSummaryViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val workoutId: String = savedStateHandle.get<String>("workoutId")!!

    private val _uiState = MutableStateFlow(WorkoutSummaryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadWorkoutSummary()
    }

    private fun loadWorkoutSummary() {
        viewModelScope.launch {
            val workoutLog = workoutRepository.getWorkoutLogById(workoutId)
            if (workoutLog != null) {
                // Here you would have more complex logic if the log contained interval breakdowns.
                // For now, we'll assume a 50/50 split of the total duration for fast/slow walk.
                val halfDurationSeconds = workoutLog.totalDurationSeconds / 2

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        totalDuration = formatDuration(workoutLog.totalDurationSeconds),
                        totalSteps = formatSteps(workoutLog.totalSteps),
                        fastWalkTime = formatDuration(halfDurationSeconds),
                        slowWalkTime = formatDuration(halfDurationSeconds)
                    )
                }
            } else {
                // Handle case where workout isn't found
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun formatDuration(seconds: Int): String {
        val minutes = TimeUnit.SECONDS.toMinutes(seconds.toLong())
        val remainingSeconds = seconds - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%02d:%02d minutes", minutes, remainingSeconds)
    }

    private fun formatSteps(steps: Int): String {
        val formatter = NumberFormat.getNumberInstance(Locale.US)
        return "${formatter.format(steps)} steps"
    }
}