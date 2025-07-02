package com.binarybricks.iwt.data.repository

import com.binarybricks.iwt.data.model.Interval
import com.binarybricks.iwt.data.model.IntervalType
import com.binarybricks.iwt.data.model.WorkoutPreset
import com.binarybricks.iwt.data.model.WorkoutLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

interface WorkoutRepository {
    fun getWorkoutPresets(): List<WorkoutPreset>
    fun getLatestWorkoutLog(): Flow<WorkoutLog?>
}

@Singleton
class FakeWorkoutRepository @Inject constructor() : WorkoutRepository {
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
        // For now, return null to simulate no workout history
        // In a real implementation, this would be fetched from a database
        return flowOf(null)

        // Example of returning mock data:
        // return flowOf(
        //     WorkoutLog(
        //         id = "workout_123",
        //         date = Date(),
        //         totalDurationSeconds = 1200, // 20 minutes
        //         totalSteps = 2500,
        //         presetName = "Beginner"
        //     )
        // )
    }
}