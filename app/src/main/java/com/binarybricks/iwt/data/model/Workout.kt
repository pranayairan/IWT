package com.binarybricks.iwt.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
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

// Type converters for Room
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromIntervalsList(value: String): List<Interval> {
        if (value.isEmpty()) return emptyList()
        return value.split(",").map {
            val (type, duration) = it.split(":")
            Interval(IntervalType.valueOf(type), duration.toInt())
        }
    }

    @TypeConverter
    fun intervalsListToString(intervals: List<Interval>): String {
        return intervals.joinToString(",") { "${it.type}:${it.durationSeconds}" }
    }
}

// Represents a selectable workout plan
@Entity(tableName = "workout_presets")
data class WorkoutPreset(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val totalDurationMinutes: Int,
    @TypeConverters(Converters::class)
    val intervals: List<Interval>
)

// Represents a completed workout log
@Entity(tableName = "workout_logs")
data class WorkoutLog(
    @PrimaryKey
    val id: String,
    @TypeConverters(Converters::class)
    val date: Date,
    val totalDurationSeconds: Int,
    val totalSteps: Int,
    val presetName: String
)