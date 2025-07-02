package com.binarybricks.iwt.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binarybricks.iwt.data.preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    // Combine multiple flows from the repository into a single UI state flow
    val uiState: StateFlow<SettingsUiState> = combine(
        preferencesRepository.soundCuesEnabled,
        preferencesRepository.vibrationCuesEnabled,
        preferencesRepository.keepScreenOnEnabled
    ) { sound, vibration, keepScreenOn ->
        SettingsUiState(
            soundCuesEnabled = sound,
            vibrationCuesEnabled = vibration,
            keepScreenOnEnabled = keepScreenOn
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun onSoundCuesToggled(isEnabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setSoundCuesEnabled(isEnabled)
        }
    }

    fun onVibrationCuesToggled(isEnabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setVibrationCuesEnabled(isEnabled)
        }
    }

    fun onKeepScreenOnToggled(isEnabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setKeepScreenOnEnabled(isEnabled)
        }
    }
}