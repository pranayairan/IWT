package com.binarybricks.iwt.data.preferences

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles user preferences for the application
 * using SharedPreferences and StateFlow to provide reactive updates.
 */
@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("iwt_user_preferences", Context.MODE_PRIVATE)

    // MutableStateFlow instances to hold current preference values
    private val _soundCuesEnabled = MutableStateFlow(
        sharedPreferences.getBoolean(KEY_SOUND_CUES_ENABLED, false)
    )
    private val _vibrationCuesEnabled = MutableStateFlow(
        sharedPreferences.getBoolean(KEY_VIBRATION_CUES_ENABLED, false)
    )
    private val _keepScreenOnEnabled = MutableStateFlow(
        sharedPreferences.getBoolean(KEY_KEEP_SCREEN_ON_ENABLED, true)
    )

    // Public StateFlow to observe preference changes
    val soundCuesEnabled: StateFlow<Boolean> = _soundCuesEnabled
    val vibrationCuesEnabled: StateFlow<Boolean> = _vibrationCuesEnabled
    val keepScreenOnEnabled: StateFlow<Boolean> = _keepScreenOnEnabled

    // SharedPreferences change listener to update StateFlow values
    private val prefListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        when (key) {
            KEY_SOUND_CUES_ENABLED -> {
                _soundCuesEnabled.value = sharedPreferences.getBoolean(key, false)
            }

            KEY_VIBRATION_CUES_ENABLED -> {
                _vibrationCuesEnabled.value = sharedPreferences.getBoolean(key, false)
            }

            KEY_KEEP_SCREEN_ON_ENABLED -> {
                _keepScreenOnEnabled.value = sharedPreferences.getBoolean(key, true)
            }
        }
    }

    init {
        // Register listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(prefListener)
    }

    // Suspend functions to update preferences
    suspend fun setSoundCuesEnabled(isEnabled: Boolean) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putBoolean(KEY_SOUND_CUES_ENABLED, isEnabled).apply()
        }
    }

    suspend fun setVibrationCuesEnabled(isEnabled: Boolean) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putBoolean(KEY_VIBRATION_CUES_ENABLED, isEnabled).apply()
        }
    }

    suspend fun setKeepScreenOnEnabled(isEnabled: Boolean) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putBoolean(KEY_KEEP_SCREEN_ON_ENABLED, isEnabled).apply()
        }
    }

    companion object {
        private const val KEY_SOUND_CUES_ENABLED = "sound_cues_enabled"
        private const val KEY_VIBRATION_CUES_ENABLED = "vibration_cues_enabled"
        private const val KEY_KEEP_SCREEN_ON_ENABLED = "keep_screen_on_enabled"
    }
}