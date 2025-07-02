package com.binarybricks.iwt.data.repository

import com.binarybricks.iwt.data.model.Interval
import com.binarybricks.iwt.data.model.IntervalType
import com.binarybricks.iwt.data.model.WorkoutPreset
import com.binarybricks.iwt.data.model.WorkoutLog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

interface WorkoutRepository {
    fun getWorkoutPresets(): List<WorkoutPreset>
    fun getLatestWorkoutLog(): Flow<WorkoutLog?>
    suspend fun getWorkoutLogById(id: String): WorkoutLog?
    suspend fun saveWorkoutLog(log: WorkoutLog): String // Returns the ID of the new log
}

@Singleton
class FakeWorkoutRepository @Inject constructor() : WorkoutRepository {

    // A fake list of logs to search through
    private val fakeLogs = mutableListOf(
        WorkoutLog(
            id = "dummy_workout_123",
            date = Date(),
            totalDurationSeconds = 1800, // 30 minutes
            totalSteps = 3210,
            presetName = "Intermediate"
        ),
        WorkoutLog(
            id = "new",
            date = Date(),
            totalDurationSeconds = 1500, // 25 minutes
            totalSteps = 2845,
            presetName = "Beginner"
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
        // Return the first workout log from our fake logs
        return flowOf(fakeLogs.firstOrNull())
    }

    // Implement the new method
    override suspend fun getWorkoutLogById(id: String): WorkoutLog? {
        // In a real app, this would be a Room DB query.
        delay(150) // Simulate network/db delay
        return fakeLogs.find { it.id == id }
    }

    override suspend fun saveWorkoutLog(log: WorkoutLog): String {
        // In a real app, this would insert into Room and return the new row's ID.
        // For the fake repo, we'll just add it to our list.
        val newId = "new_workout_${System.currentTimeMillis()}"
        val newLog = log.copy(id = newId)
        fakeLogs.add(0, newLog) // Add to the start of the list so it appears as the latest
        delay(100) // Simulate network/db delay
        return newId
    }
}