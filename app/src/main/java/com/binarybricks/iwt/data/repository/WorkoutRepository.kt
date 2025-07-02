package com.binarybricks.iwt.data.repository

import com.binarybricks.iwt.data.model.Interval
import com.binarybricks.iwt.data.model.IntervalType
import com.binarybricks.iwt.data.model.WorkoutPreset
import com.binarybricks.iwt.data.model.WorkoutLog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

interface WorkoutRepository {
    fun getWorkoutPresets(): List<WorkoutPreset>
    fun getLatestWorkoutLog(): Flow<WorkoutLog?>
    suspend fun getWorkoutLogById(id: String): WorkoutLog?
    suspend fun saveWorkoutLog(log: WorkoutLog): String // Returns the ID of the new log
    fun getAllWorkoutLogs(): Flow<List<WorkoutLog>>
}

@Singleton
class FakeWorkoutRepository @Inject constructor() : WorkoutRepository {

    // Use MutableStateFlow for reactive updates
    private val _fakeLogs = MutableStateFlow(
        mutableListOf(
            WorkoutLog(
                id = "dummy_workout_123",
                date = Date(),
                totalDurationSeconds = 1800, // 30 minutes
                totalSteps = 3210,
                presetName = "Intermediate"
            ),
            WorkoutLog(
                id = "new",
                date = Date(System.currentTimeMillis() - 86400000), // Yesterday
                totalDurationSeconds = 1500, // 25 minutes
                totalSteps = 2845,
                presetName = "Beginner"
            ),
            // Add a few more logs with different dates
            WorkoutLog(
                id = "workout_history_1",
                date = Date(System.currentTimeMillis() - 2 * 86400000), // 2 days ago
                totalDurationSeconds = 2400, // 40 minutes
                totalSteps = 5120,
                presetName = "Advanced"
            ),
            WorkoutLog(
                id = "workout_history_2",
                date = Date(System.currentTimeMillis() - 4 * 86400000), // 4 days ago
                totalDurationSeconds = 1200, // 20 minutes
                totalSteps = 2100,
                presetName = "Beginner"
            ),
            WorkoutLog(
                id = "workout_history_3",
                date = Date(System.currentTimeMillis() - 7 * 86400000), // 7 days ago
                totalDurationSeconds = 1980, // 33 minutes
                totalSteps = 3850,
                presetName = "Intermediate"
            )
        )
    )

    override fun getWorkoutPresets(): List<WorkoutPreset> {
        return listOf(
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
    }

    override fun getLatestWorkoutLog(): Flow<WorkoutLog?> {
        // Use the getAllWorkoutLogs flow to get the latest
        return getAllWorkoutLogs().map { logs -> logs.firstOrNull() }
    }

    // Implement the new method
    override suspend fun getWorkoutLogById(id: String): WorkoutLog? {
        // In a real app, this would be a Room DB query.
        delay(150) // Simulate network/db delay
        return _fakeLogs.value.find { it.id == id }
    }

    override suspend fun saveWorkoutLog(log: WorkoutLog): String {
        // In a real app, this would insert into Room and return the new row's ID.
        val newId = "new_workout_${System.currentTimeMillis()}"
        val newLog = log.copy(id = newId)

        // Update the flow with the new list
        val currentLogs = _fakeLogs.value.toMutableList()
        currentLogs.add(0, newLog) // Add to the start of the list so it appears as the latest
        _fakeLogs.value = currentLogs

        delay(100) // Simulate network/db delay
        return newId
    }

    // Implement the new method to get all workout logs
    override fun getAllWorkoutLogs(): Flow<List<WorkoutLog>> {
        // Return logs in descending order by date
        return _fakeLogs.map { logs -> logs.sortedByDescending { it.date } }
    }
}