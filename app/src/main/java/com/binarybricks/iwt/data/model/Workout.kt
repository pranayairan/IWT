package com.binarybricks.iwt.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

// Represents a single interval within a workout
@Parcelize
data class Interval(
    val type: IntervalType,
    val durationSeconds: Int
) : Parcelable

enum class IntervalType {
    WARM_UP,
    FAST_WALK,
    SLOW_WALK,
    COOL_DOWN
}

// Represents a selectable workout plan
data class WorkoutPreset(
    val id: String,
    val name: String,
    val description: String,
    val totalDurationMinutes: Int,
    val intervals: List<Interval>
)

// Represents a completed workout log
data class WorkoutLog(
    val id: String,
    val date: Date,
    val totalDurationSeconds: Int,
    val totalSteps: Int,
    val presetName: String
)