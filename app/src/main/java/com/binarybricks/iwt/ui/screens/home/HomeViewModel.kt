package com.binarybricks.iwt.ui.screens.home

import com.binarybricks.iwt.ui.screens.home.HomeUiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binarybricks.iwt.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Note: We'll use Hilt after fixing dependencies
class HomeViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val presets = workoutRepository.getWorkoutPresets()
        _uiState.update { it.copy(presets = presets) }

        viewModelScope.launch {
            workoutRepository.getLatestWorkoutLog().collect { log ->
                _uiState.update { it.copy(lastWorkout = log) }
            }
        }
    }

    fun onPresetSelected(presetId: String) {
        _uiState.update { it.copy(selectedPresetId = presetId) }
    }

    fun onPermissionResult(isGranted: Boolean) {
        _uiState.update { it.copy(isActivityPermissionGranted = isGranted) }
    }
}