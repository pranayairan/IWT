package com.binarybricks.iwt.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import com.binarybricks.iwt.R
import com.binarybricks.iwt.data.model.Interval
import com.binarybricks.iwt.data.model.IntervalType
import com.binarybricks.iwt.data.preferences.UserPreferencesRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import java.util.LinkedList
import javax.inject.Inject

// Data class to hold the live state of the workout
data class WorkoutServiceState(
    val currentInterval: Interval? = null,
    val intervalTimeLeftSeconds: Int = 0,
    val totalTimeElapsedSeconds: Int = 0,
    val steps: Int = 0,
    val isPaused: Boolean = true,
    val isDone: Boolean = false
)

@AndroidEntryPoint
class IwtWorkoutService : Service(), SensorEventListener {

    // Inject the preferences repository
    @Inject
    lateinit var preferencesRepository: UserPreferencesRepository

    private val binder = LocalBinder()
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var timerJob: Job? = null

    // Sensor-related variables
    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var initialSteps = -1

    // The single source of truth for the workout state
    companion object {
        private val _serviceState = MutableStateFlow(WorkoutServiceState())
        val serviceState = _serviceState.asStateFlow()

        const val ACTION_START = "ACTION_START"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_RESUME = "ACTION_RESUME"
        const val ACTION_STOP = "ACTION_STOP"
        const val EXTRA_INTERVALS = "EXTRA_INTERVALS"
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_CHANNEL_ID = "IWT_WORKOUT_CHANNEL"
    }

    inner class LocalBinder : Binder() {
        fun getService(): IwtWorkoutService = this@IwtWorkoutService
    }

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        createNotificationChannel()
    }

    override fun onBind(intent: Intent): IBinder = binder

    @Suppress("DEPRECATION")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action?.let { action ->
            when (action) {
                ACTION_START -> {
                    // Extract intervals from the bundle
                    val extras = intent.extras
                    if (extras != null) {
                        val intervals = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            extras.getParcelableArrayList(EXTRA_INTERVALS, Interval::class.java)
                        } else {
                            @Suppress("DEPRECATION")
                            extras.getParcelableArrayList<Interval>(EXTRA_INTERVALS)
                        }

                        if (intervals != null) {
                            startWorkout(intervals)
                        }
                    }
                }
                ACTION_PAUSE -> pauseTimer()
                ACTION_RESUME -> resumeTimer()
                ACTION_STOP -> stopWorkout()
            }
        }
        return START_STICKY
    }

    private fun startWorkout(intervals: List<Interval>) {
        val intervalQueue = LinkedList(intervals)
        _serviceState.value = WorkoutServiceState(isPaused = false) // Reset state

        // Create notification and start foreground service with the appropriate type
        val notification = createNotification("Starting workout...")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        registerStepCounter()

        timerJob = serviceScope.launch {
            while (intervalQueue.isNotEmpty() && isActive) {
                val current = intervalQueue.poll()!!
                _serviceState.value = _serviceState.value.copy(
                    currentInterval = current,
                    intervalTimeLeftSeconds = current.durationSeconds
                )

                // Countdown for the current interval
                for (t in current.durationSeconds downTo 1) {
                    while (_serviceState.value.isPaused) {
                        delay(500)
                    } // Check for pause
                    _serviceState.value = _serviceState.value.copy(
                        intervalTimeLeftSeconds = t,
                        totalTimeElapsedSeconds = _serviceState.value.totalTimeElapsedSeconds + 1
                    )
                    updateNotification()
                    delay(1000)
                }

                // Signal the user when an interval ends
                signalIntervalChange()
            }
            // Workout finished
            _serviceState.value = _serviceState.value.copy(isDone = true)
            stopWorkout()
        }
    }

    private fun signalIntervalChange() {
        serviceScope.launch {
            if (preferencesRepository.soundCuesEnabled.first()) {
                // Play a short tone
                ToneGenerator(
                    AudioManager.STREAM_NOTIFICATION,
                    100
                ).startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 200)
            }
            if (preferencesRepository.vibrationCuesEnabled.first()) {
                // Vibrate
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (vibrator.hasVibrator()) {
                    // Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(
                            VibrationEffect.createOneShot(
                                500,
                                VibrationEffect.DEFAULT_AMPLITUDE
                            )
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        vibrator.vibrate(500)
                    }
                }
            }
        }
    }

    private fun pauseTimer() {
        _serviceState.value = _serviceState.value.copy(isPaused = true)
    }

    private fun resumeTimer() {
        _serviceState.value = _serviceState.value.copy(isPaused = false)
    }

    private fun stopWorkout() {
        timerJob?.cancel()
        unregisterStepCounter()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(Service.STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
        stopSelf()
    }

    // --- SENSOR LOGIC ---
    private fun registerStepCounter() {
        initialSteps = -1
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun unregisterStepCounter() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                if (initialSteps == -1) {
                    initialSteps = it.values[0].toInt()
                }
                val currentSteps = it.values[0].toInt() - initialSteps
                _serviceState.value = _serviceState.value.copy(steps = currentSteps)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}


    // --- NOTIFICATION LOGIC ---
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "IWT Workout",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    private fun createNotification(text: String): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Interval Walking Training")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .build()
    }

    private fun updateNotification() {
        val state = _serviceState.value
        val text =
            "Interval: ${state.currentInterval?.type} | Time left: ${state.intervalTimeLeftSeconds}s"
        val notification = createNotification(text)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}