package com.binarybricks.iwt.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binarybricks.iwt.data.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class WorkoutHistoryViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    val uiState: StateFlow<WorkoutHistoryUiState> = workoutRepository.getAllWorkoutLogs()
        .map { logs ->
            // Map the data model (WorkoutLog) to the UI model (WorkoutHistoryItem)
            val items = logs.map { log ->
                WorkoutHistoryItem(
                    id = log.id,
                    date = formatDate(log.date),
                    duration = formatDuration(log.totalDurationSeconds),
                    steps = formatSteps(log.totalSteps)
                )
            }
            WorkoutHistoryUiState(isLoading = false, historyItems = items)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WorkoutHistoryUiState(isLoading = true)
        )

    private fun formatDate(date: Date): String {
        // Example format: "June 28, 2025"
        val format = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        return format.format(date)
    }

    private fun formatDuration(seconds: Int): String {
        val minutes = TimeUnit.SECONDS.toMinutes(seconds.toLong())
        return "Total Duration: $minutes minutes"
    }

    private fun formatSteps(steps: Int): String {
        return "Total Steps: $steps"
    }
}