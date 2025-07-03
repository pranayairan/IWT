package com.binarybricks.iwt.data.repository

import com.binarybricks.iwt.data.database.WorkoutDao
import com.binarybricks.iwt.data.model.Interval
import com.binarybricks.iwt.data.model.IntervalType
import com.binarybricks.iwt.data.model.WorkoutLog
import com.binarybricks.iwt.data.model.WorkoutPreset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomWorkoutRepository @Inject constructor(
    private val workoutDao: WorkoutDao
) : WorkoutRepository {

    // Cache of default presets
    private val defaultPresets = listOf(
        WorkoutPreset(
            id = "beginner_20",
            name = "Beginner",
            description = "20 Minutes",
            totalDurationMinutes = 20,
            intervals = listOf(
                Interval(IntervalType.WARM_UP, 180),     // 3 min warm up
                Interval(IntervalType.FAST_WALK, 60),    // 1 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.FAST_WALK, 60),    // 1 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.FAST_WALK, 60),    // 1 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.FAST_WALK, 60),    // 1 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.FAST_WALK, 60),    // 1 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.FAST_WALK, 60),    // 1 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.FAST_WALK, 60),    // 1 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.COOL_DOWN, 180)    // 3 min cool down
            )
        ),
        WorkoutPreset(
            id = "intermediate_30",
            name = "Intermediate",
            description = "30 Minutes",
            totalDurationMinutes = 30,
            intervals = listOf(
                Interval(IntervalType.WARM_UP, 180),     // 3 min warm up
                Interval(IntervalType.FAST_WALK, 90),    // 1.5 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.FAST_WALK, 90),    // 1.5 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.FAST_WALK, 90),    // 1.5 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.FAST_WALK, 90),    // 1.5 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.FAST_WALK, 90),    // 1.5 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.FAST_WALK, 90),    // 1.5 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.FAST_WALK, 90),    // 1.5 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.FAST_WALK, 90),    // 1.5 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.COOL_DOWN, 180)    // 3 min cool down
            )
        ),
        WorkoutPreset(
            id = "advanced_40",
            name = "Advanced",
            description = "40 Minutes",
            totalDurationMinutes = 40,
            intervals = listOf(
                Interval(IntervalType.WARM_UP, 300),     // 5 min warm up
                Interval(IntervalType.FAST_WALK, 120),   // 2 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.FAST_WALK, 120),   // 2 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.FAST_WALK, 120),   // 2 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.FAST_WALK, 120),   // 2 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.FAST_WALK, 120),   // 2 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.FAST_WALK, 120),   // 2 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.FAST_WALK, 120),   // 2 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.FAST_WALK, 120),   // 2 min fast
                Interval(IntervalType.SLOW_WALK, 60),    // 1 min slow
                Interval(IntervalType.COOL_DOWN, 300)    // 5 min cool down
            )
        )
    )

    init {
        // Insert default presets into the database when repository is created
        CoroutineScope(Dispatchers.IO).launch {
            workoutDao.insertWorkoutPresets(defaultPresets)
        }
    }

    override fun getWorkoutPresets(): List<WorkoutPreset> {
        // First try to get from Room database
        val dbPresets = try {
            workoutDao.getAllWorkoutPresets()
        } catch (e: Exception) {
            // Fall back to default presets if database access fails
            defaultPresets
        }

        // If no presets in database, return default presets
        return if (dbPresets.isEmpty()) defaultPresets else dbPresets
    }

    override fun getLatestWorkoutLog(): Flow<WorkoutLog?> {
        return try {
            workoutDao.getLatestWorkoutLog()
        } catch (e: Exception) {
            emptyFlow()
        }
    }

    override suspend fun getWorkoutLogById(id: String): WorkoutLog? {
        return try {
            workoutDao.getWorkoutLogById(id)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun saveWorkoutLog(log: WorkoutLog): String {
        val newId = if (log.id.isBlank()) "workout_${System.currentTimeMillis()}" else log.id
        val logToSave = log.copy(id = newId)

        try {
            workoutDao.insertWorkoutLog(logToSave)
        } catch (e: Exception) {
            // Handle error (log it, etc.)
        }

        return newId
    }

    override fun getAllWorkoutLogs(): Flow<List<WorkoutLog>> {
        return try {
            workoutDao.getAllWorkoutLogs()
        } catch (e: Exception) {
            emptyFlow()
        }
    }
}