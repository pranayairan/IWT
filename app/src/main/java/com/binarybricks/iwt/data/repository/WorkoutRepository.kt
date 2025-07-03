package com.binarybricks.iwt.data.repository

import com.binarybricks.iwt.data.model.WorkoutLog
import com.binarybricks.iwt.data.model.WorkoutPreset
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getWorkoutPresets(): List<WorkoutPreset>
    fun getLatestWorkoutLog(): Flow<WorkoutLog?>
    suspend fun getWorkoutLogById(id: String): WorkoutLog?
    suspend fun saveWorkoutLog(log: WorkoutLog): String // Returns the ID of the new log
    fun getAllWorkoutLogs(): Flow<List<WorkoutLog>>
}