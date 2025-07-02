package com.binarybricks.iwt.ui.screens.settings

data class SettingsUiState(
    val soundCuesEnabled: Boolean = false,
    val vibrationCuesEnabled: Boolean = false,
    val keepScreenOnEnabled: Boolean = true
)